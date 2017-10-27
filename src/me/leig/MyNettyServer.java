package me.leig;

public class MyNettyServer {

    public void startServer(MyServiceMessage myMessage) throws Exception {

        NettyMessage nettyMessage = new NettyMessage();

        nettyMessage.setMsgType(DefConf.FIRSTCONNECT);
        nettyMessage.setSenderId("1001");
        String msg = "192.168.26.168@8099@" + DefConf.STATUE_OK;
        nettyMessage.setData(msg.getBytes());
        NettyServer nettyServer = new NettyServer(8099, myMessage,
                nettyMessage);

        nettyServer.startConnect();
    }

}
