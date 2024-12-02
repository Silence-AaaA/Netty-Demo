package com.netty.NIO.nioConnectBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;


public class Server {

    //创建集合，存储对应的客户端信息

    public static ArrayList<SocketChannel> socketChannels = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        //设置byteBuffer缓冲区存储数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //1。注册连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.设置监听端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        while (true) {
            //3.创建与客户端之间的连接
            SocketChannel accept = serverSocketChannel.accept();
            //添加数据
            socketChannels.add(accept);
            for (SocketChannel socketChannel : socketChannels) {
                //获取数据
                socketChannel.read(buffer.flip());
                buffer.flip();
                System.out.println(buffer);
                //清空数据变为读取 清空数据
                buffer.clear();
            }
        }
    }
}
