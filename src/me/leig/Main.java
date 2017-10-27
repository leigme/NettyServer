package me.leig;

public class Main {

    public static void main(String[] args) throws Exception {
        MyServiceMessage myMessage = new MyServiceMessage("1001");
        MyNettyServer nettyServer = new MyNettyServer();
        nettyServer.startServer(myMessage);
    }
}
