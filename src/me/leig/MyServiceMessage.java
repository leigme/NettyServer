package me.leig;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyServiceMessage implements MessageListener {

    private static Logger log = Logger.getLogger(MyServiceMessage.class);

    private String senderId;
    private NettyMessage mNettyMessage;
    private Map<String, ChannelHandlerContext> mContextMap;

    public MyServiceMessage(String senderId) {
        this.senderId = senderId;
        mContextMap = new ConcurrentHashMap<>();
    }

    @Override
    public void sendMessage(List<String> list, int i, String s) {
        for (String receiveId: list) {
            ChannelHandlerContext receiveCtx = mContextMap.get(receiveId);
            if (null != receiveCtx) {
                mNettyMessage = new NettyMessage(senderId, receiveId, i, s.getBytes());
                receiveCtx.channel().writeAndFlush(mNettyMessage);
            }
        }
    }

    @Override
    public void sendMessage(String s, int i, String s1) {
        if (DefConf.RECEIVE_ALL.equals(s)) {

        } else {
            ChannelHandlerContext ctx = mContextMap.get(s);
            if (null != ctx) {
                mNettyMessage = new NettyMessage(senderId, s, i, s1.getBytes());
                ctx.channel().writeAndFlush(mNettyMessage);
            }
        }
    }

    @Override
    public void receiveMessage(NettyMessage nettyMessage) {
        String senderId = nettyMessage.getSenderId();
        String receiveId = nettyMessage.getReceiveId();

        for (Map.Entry<String, ChannelHandlerContext> entry : mContextMap.entrySet()) {
            if (null != entry.getValue() && !senderId.equals(entry.getKey())) {
                mNettyMessage = new NettyMessage(senderId, receiveId, 3,
                        nettyMessage.getData());
                entry.getValue().channel().writeAndFlush(mNettyMessage);
            }
        }

        log.info("[" + senderId + "]对[" + receiveId + "]说了{" + new String
                (nettyMessage.getData
                ()) + "}");
    }

    @Override
    public void saveCtxData(CtxDataBase ctxDataBase) {
        mContextMap.put(ctxDataBase.getUserId(), ctxDataBase.getCtx());

        String userIds = "";

        // 获取保存的用户数据
        for (Map.Entry<String, ChannelHandlerContext> entry : mContextMap
                .entrySet()) {
            if (!ctxDataBase.getUserId().equals(entry.getKey())) {
                userIds += entry.getKey() + "@";
            }
        }

        // 发送用户集合
        for (Map.Entry<String, ChannelHandlerContext> entry : mContextMap.entrySet()) {
            if (!ctxDataBase.getUserId().equals(entry.getKey())) {
                if (null != entry.getValue()) {
                    mNettyMessage = new NettyMessage();
                    mNettyMessage.setSenderId("1001");
                    mNettyMessage.setReceiveId(entry.getKey());
                    mNettyMessage.setMsgType(1004);
                    mNettyMessage.setData(userIds.getBytes());
                    entry.getValue().channel().writeAndFlush(mNettyMessage);
                }
            }
        }
    }
}
