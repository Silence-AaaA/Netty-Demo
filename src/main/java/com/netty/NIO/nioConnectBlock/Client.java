package com.netty.NIO.nioConnectBlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

//客户端
public class Client {
    public static void main(String[] args) throws IOException {
        //1.创建连接通道
        SocketChannel socketChannel = SocketChannel.open();
        //2.设置连接服务器的地址
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        System.out.println("waiting for connection");
        socketChannel.write(Charset.defaultCharset().encode("Hello World!"));
    }
}
