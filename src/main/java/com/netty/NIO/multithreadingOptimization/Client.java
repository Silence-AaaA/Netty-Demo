package com.netty.NIO.multithreadingOptimization;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        ByteBuffer bufferBuffer = Charset.defaultCharset().encode( "Hello!Silence");
        sc.write(bufferBuffer);
        System.out.println("Client connected over");
        System.in.read();
    }
}
