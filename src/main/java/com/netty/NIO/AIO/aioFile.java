package com.netty.NIO.AIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

//异步非阻塞IO 传输文件信息
public class aioFile {
    private static final Logger log = LoggerFactory.getLogger(aioFile.class);

    public static void main(String[] args) throws IOException{
        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"),StandardOpenOption.READ)) {
            /**
             * 需要四个参数
             * 1.Bytebuffer读取数据
             * 2.文件读取的其实位置
             * 3.附件
             * 4.回调对象 ，回调对象用户给我们当前任务返还我们所需要的结果
             */
            log.debug("read begin");
            ByteBuffer buffer = ByteBuffer.allocate(20);
            channel.read(buffer,0,buffer,new CompletionHandler<Integer, ByteBuffer>(){
                //回调函数成功完成
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println(result);
                    log.debug("read complete");
                    //读取数据
                    attachment.flip();
                    System.out.println(attachment.toString());
                }

                //回调函数失败，未完成
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.error("read failed, ex: {}", exc);
                }
            });
            log.debug("read end");
            //为什么是异步调用呢
            //因为这里我们发现 read begin 与 read end是一起出现的，之后出现的才是在对应的回调函数当中写的内容！
            //由此可见是异步 并且是非阻塞的，不会在这个回到函数上消耗时间
            //有一点需要明确知道，异步与非阻塞是一起的
            //永远都不存在异步阻塞这样的存在！！！
        }
    }
}
