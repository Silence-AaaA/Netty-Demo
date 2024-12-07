package com.netty.NIO.nioConnectBlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

//非阻塞模式下一直获取连接不停止，

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    //创建集合，存储对应的客户端信息
    public static ArrayList<SocketChannel> socketChannels = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        //0.设置byteBuffer缓冲区存储数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //1。注册连接 创建连接通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //1.1 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //2.设置监听端口
        serverSocketChannel.bind(new InetSocketAddress(8080));
        while (true) {
            log.debug("connecting");
            //3.创建与客户端之间的连接 每有一个客户端连接，都会从这里进行监听
            //3.1 非阻塞模式下，说客户端与服务端连接的建立在这里不会堵塞，会直接通行，但是这里如果没有对应的客户端访问
            //那么返还值就为NULL，根据这个我们可以加一些判断对于这些空值进行处理
            SocketChannel accept = serverSocketChannel.accept();
            if (accept != null) {
                //添加数据
                socketChannels.add(accept);
            }
            for (SocketChannel socketChannel : socketChannels) {
                //获取数据
                log.debug("准备读取数据了！");
                //在非阻塞模式下，这里的读取也不会再停止，对应的会继续运行，如果没有读取到数据将会返还为空
                int read = socketChannel.read(buffer.flip());
                //读取数据
                buffer.flip();
                if (read!=0){
                    log.debug(String.valueOf(buffer));
                    log.debug("数据读取完毕");
                }
                //清空数据变为读取 清空数据
                buffer.clear();
            }
        }
    }
}
