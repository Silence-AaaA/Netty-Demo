package com.netty.Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1。设置启动类
        new Bootstrap()
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
                .connect(new InetSocketAddress("127.0.0.1", 8080))
                /** 3.如果没有建立连接，那么这里实际上是处于阻塞状态的，并不会向下继续执行操作 **/
                .sync()
                .channel()
                /**
                 * 4.向服务器发送数据
                 *这个发送的字符串，之后会被我们在配置器上初始化设置的配置进行处理
                 *我们添加了一个处理，编码器，会将我们当前的字符转转化为字节码进行处理：讲字符串转化为字节码形式进行传输
                 */
                .writeAndFlush("hello!world");
    }
}
