package com.netty.NIO.nioConnectBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

//客户端
public class Client {
    public static void main(String[] args) throws IOException {
        //1.创建连接通道
        SocketChannel socketChannel = SocketChannel.open();
        //2.设置连接服务器的地址
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        System.out.println("waiting for connection");
    }
}
