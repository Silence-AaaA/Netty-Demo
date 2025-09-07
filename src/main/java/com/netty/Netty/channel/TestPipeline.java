package com.netty.Netty.channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        EventLoopGroup group = new DefaultEventLoopGroup(2);

        new ServerBootstrap()
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();

                        // 添加处理 head - > .......tile 前面有一个head 后面一个head

                        pipeline.addLast("h1",new ChannelInboundHandlerAdapter(){
                            //设置输入前pipline处理器
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("channelRead1");
                                //唤醒下一个入栈处理器
                                //将当前这个handle处理之后的结果继续向下传递
                                super.channelRead(ctx, msg);
                            }
                        });

                        pipeline.addLast("h2",new ChannelInboundHandlerAdapter(){
                            //设置输入前pipline处理器
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("channelRead2");
                                super.channelRead(ctx, msg);
                            }
                        });

                        pipeline.addLast("h3",new ChannelInboundHandlerAdapter(){
                            //设置输入前pipline处理器
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("channelRead3");

                                ByteBuf byteBuf = (ByteBuf)msg;
                                //输出日志信息
                                log.debug(byteBuf.toString(Charset.defaultCharset()));

                                //将信息写入到出栈当中
                                // 一种方式是通过channel进行调用，是从tail尾巴的位置向前面找  出栈处理器的
                                pipeline.writeAndFlush(ctx.alloc().buffer().writeBytes(byteBuf));
                                //另外一种是通过ctx调用，这种调用是从当前  入栈位置向前赵 出栈处理器的
                                //ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(byteBuf));
                            }
                        });

                        //对于输出的 是tail向前面读取的，从后往前进行
                        // 所有的出栈处理器 只有你写入了数据 才能触发
                        pipeline.addLast("h4",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("write1");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h5",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("write2");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h6",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("write3");
                                super.write(ctx, msg, promise);
                            }
                        });

                    }
                })
                .bind(8080);
    }
}
