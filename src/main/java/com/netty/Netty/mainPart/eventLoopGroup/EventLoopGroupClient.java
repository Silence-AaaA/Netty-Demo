package com.netty.Netty.mainPart.eventLoopGroup;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class EventLoopGroupClient {
    /**
     * 这个例子当中我们可以知道，一个Client客户端建立起来的一个Channel对象，对应一个永远对他进行负责的EventLoopGroup当中的一个EventLoop对象
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {
        //1。设置启动类
        ChannelFuture channelFuture = new Bootstrap()
                //2.设置事件处理组
                .group(new NioEventLoopGroup())
                //3.选择客户端channel实现
                .channel(NioSocketChannel.class)
                //4/配置器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    /** 2. 建立连接之后初始化客户端方法 */
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //对于发送的数据进行编码
                        //2 - 设置编码器 使用编码器将字符串转化为字节形式存储
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                /** 1.建立连接 */
                // 这里的connect 是一个异步非阻塞的，真正去建立链接的，是上面的事件处理组 NioEventLoopGroup
                .connect(new InetSocketAddress("127.0.0.1", 8080));

                /** 3.如果没有建立连接，那么这里实际上是处于阻塞状态的，并不会向下继续执行操作 **/
                //2.1 要注意 这里之所以阻塞，就是因为调用了同步方法，channelFuture的sync，进行阻塞，直到NIO线程链接建立完毕
                //channelFuture.sync();
                //Channel channel = channelFuture.channel();
                //// 这里使用write 跟 writeAndFlush是不一样的
                //channel.writeAndFlush("hello world!");

                //2.2 方法2，上面的sync使用的是一种阻塞方法，也就是在当前主线程中执行；除此之外，还有一种方式为异步的，使用addListener
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        //这里的channelFuture 跟我们上面的其实是一个
                        //我们将通道连接的创建以及结果的处理都交给NIO线程进行了，我们的主线程不再执行
                        Channel channel = channelFuture.channel();
                        channel.writeAndFlush("use NIO method");
                    }
                });
    }
}
