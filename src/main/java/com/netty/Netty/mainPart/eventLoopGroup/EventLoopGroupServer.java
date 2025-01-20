package com.netty.Netty.mainPart.eventLoopGroup;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class EventLoopGroupServer {
    private static final Logger log = LoggerFactory.getLogger(EventLoopGroupServer.class);

    public static void main(String[] args) {
        //2.工作细分 为了处理一个比较耗时的handler 我们选择再创建一个用来专门处理平常任务的事件循环组
        EventLoopGroup group = new DefaultEventLoopGroup(2);

        new ServerBootstrap()
                //1.工作细分：在这里为了提高效率，我们对于EventLoopGroup进行进一步的细分，一个用来进行accept接受客户端，一个用来对客户端的操作进行处理
                //这里我们进行accept的操作实际上只需要一个对应的线程，那么这里生成的BOSS对应实体数量实际上就是1个，之后的workers设置为2个
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //调用初始化方法 添加流水线，流水线当中包含多个的Channel对象
                        //nioSocketChannel.pipeline().addLast(new StringEncoder()); //这里如果在流水线当中加入了这个操作，那么就会直接对ByteBuf对象进行编码 转化为字符串
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            //这里我们的服务端关注的是对应客户端发来的消息，那么就需要进行读取事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //此处的msg实际上就是对应发送过来的信息，但是是ByteBuf的形式存在的
                                //因为我们没有对其做前置处理
                                ByteBuf byteBuf = (ByteBuf)msg;
                                //转化为字符串，并且使用的是当前电脑默认的编码集
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                                //这里我们需要将经过当前handler处理之后的信息交给之后的handler
                                ctx.fireChannelRead(msg);
                            }
                            //使用另外一个专门用来处理平常工作的事件循环组处理这些事件，从而做到不阻塞其他任务的进行
                        }).addLast(group,"normalEvent1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf)msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
