package com.netty.NIO.nioSelectorConnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

//非阻塞模式下一直获取连接不停止，可能就会导致其累死，理想状态下的连接，依旧是来一个客户端再进行连接，这样也不会消耗多余的资源

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    //创建集合，存储对应的客户端信息
    public static void main(String[] args) throws IOException {
        //1.创建Selector
        Selector selector = Selector.open();
        //2.创建服务端通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
        //3.创建客户端与Selector之间的连接,将两者之间建立连接
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //4.建立连接之后就需要绑定对应的channel的事件类型，事件类型包括四种:accept connect read write 是哪一种事件需要我们自己进行绑定
        //这里我们这个SelectionKey作为管理员，只需要关注对应的客户端是否建立连接即可
        //我们设置当前的KEY用来专门管理客户端的连接 accept()
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            //5.使用select进行检查，如果没有事件发生就在这里阻塞，有事件发生才会继续进行 这里类似一个监听器，如果有连接这种事件发生才会执行之后的操作
            selector.select();
            //6.使用迭代器处理发生的事件 selectKeys当中会存储所有的KEY，这里如果我们想要对其进行更多的操作，例如删除。那么就必须使用到迭代器
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //获取KEY
                SelectionKey key = iterator.next();
                //移除当前KEY值
                iterator.remove();
                //判断对应的KEY的类型
                if (key.isAcceptable()) {
                    log.debug("Accept Selected key: {}", key);
                    //6.使用KEY获取对应的SSC之后创建连接
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    //创建连接，返还客户端连接通道
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    SelectionKey ssKey = sc.register(selector, SelectionKey.OP_READ);
                    //绑定事件
                    ssKey.interestOps(SelectionKey.OP_READ);
                    log.debug("Accept connection");
                } else if (key.isReadable()) {
                    //如果对应的KEY是读取类型的
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.read(buffer);
                    buffer.flip();
                    System.out.println(buffer);
                    buffer.compact();
                }else if(key.isWritable()){
                    //如果对应的KEY是写入类型的，那么直接在这里写入数据即可
                }

            }
        }
    }
}
