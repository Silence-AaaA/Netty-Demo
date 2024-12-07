package com.netty.NIO.nioSelectorConnect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

//客户端
public class WriteClient {
    public static void main(String[] args) throws IOException {
        //1.创建连接通道
        SocketChannel socketChannel = SocketChannel.open();
        //2.设置连接服务器的地址
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        System.out.println("waiting for connection");

        //接收数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            int read = socketChannel.read(buffer);
            if(read == 0){
                break;
            }
            System.out.println(read);
            buffer.clear();
        }
    }
}
