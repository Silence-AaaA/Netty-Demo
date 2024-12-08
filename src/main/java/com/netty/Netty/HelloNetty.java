package com.netty.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

//开始Netty编程
public class HelloNetty {
    public static void main(String[] args) {
        //1.服务器，开始客户端服务，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.设置事件组
                .group(new NioEventLoopGroup())
                //3.选择Server服务器的ServerSocketChannel实现方式
                /**
                 * 5. 获取read 读取事件
                 * 这里是由某一个EventLoop进行处理，在这里检查对应的操作，
                 例如接收操作accept 以及对应的读取操作，都可以在这里读取到，之后进行下一步的操作执行
                 */
                .channel(NioServerSocketChannel.class)
                //4。分工 boss负责处理连接，对应的worker负责处理读写操作  并且进行初始化操作
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    /**2。不仅仅是初始化客户端的连接，客户端与服务端的初始化基本是一起进行的 **/
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //设置解码，文件传输过来的数据都是字节形式的
                        //4 - 接收到read操作之后，将对应的字节码文件使用配置类进行解码，变为字符串
                        /**6.调用配置类，将对应的读取到的数据通过解码转变为字符转的形式 **/
                        ch.pipeline().addLast(new StringDecoder());
                        //5 - 读操作之后执行我们自定义的业务处理配置
                        /**7.使用我们自定义的配置类，在我们自定义的配置类当中对于读写的数据进行输出了  **/
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换好的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                })
                //设置端口
                .bind(8080);
    }
}
