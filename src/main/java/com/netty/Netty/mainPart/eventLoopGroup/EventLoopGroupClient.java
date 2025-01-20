package com.netty.Netty.mainPart.eventLoopGroup;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class EventLoopGroupClient {
    /**
     * 这个例子当中我们可以知道，一个Client客户端建立起来的一个Channel对象，对应一个永远对他进行负责的EventLoopGroup当中的一个EventLoop对象
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {
        //1。设置启动类
        Channel channel = new Bootstrap()
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
                .channel();
        //获取到Channel对象
        System.out.println(channel);
        System.out.println("");  //这里我们打单个断点，不影响其他线程的正常进行

    }
}
