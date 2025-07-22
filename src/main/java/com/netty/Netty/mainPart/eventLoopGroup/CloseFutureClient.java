package com.netty.Netty.mainPart.eventLoopGroup;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    /** 2. 建立连接之后初始化客户端方法 */
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //对于发送的数据进行编码
                        //2 - 设置编码器 使用编码器将字符串转化为字节形式存储
                        ch.pipeline().addLast(new StringEncoder());
                        // 新增Handler 能够打印当前channel的执行流程
                        ch.pipeline().addLast(new LoggingHandler());
                    }
                }).connect(new InetSocketAddress("localhost", 8080));
        //等待获取连接
        channelFuture.sync();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                if ("q".equals(input)){
                    //关闭连接
                    channelFuture.channel().close().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            Channel channel = channelFuture.channel();
                            //执行关闭操作 确保打印跟关闭是先关闭 之后打印
                            // 这里简单说就是新开了一个线程 让其来监控关闭
                            channel.close();
                            // 在这种情况下，它的关闭跟打印才都是在一个线程当中进行的
                            // 这跟上面等待获取连接其实是一样的
                            log.debug("channel任务真正关闭处理{}", channel);
                        }
                    });
                    try {
                        ChannelFuture sync = channelFuture.channel().close().sync();
                        log.debug("阻塞式的关闭线程");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                else {
                    Channel channel = channelFuture.channel();
                    //刷新并且直接输出
                    channel.writeAndFlush(input);
                }
            }
        }).start();

        Channel channel = channelFuture.channel();
        //使用closeFuture 确保关闭之后代码的正确执行 依旧是两种方式
        channel.closeFuture().sync();
        //channel.closeFuture().addListener()
    }
}
