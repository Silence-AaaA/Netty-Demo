package com.netty.NIO.AIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

//异步非阻塞IO 传输文件信息
public class aioFile {
    public static void main(String[] args) throws IOException{
        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"),StandardOpenOption.READ)) {
            /**
             * 需要四个参数
             * 1.Bytebuffer读取数据
             * 2.文件读取的其实位置
             * 3.附件
             * 4.回调对象 ，回调对象用户给我们当前任务返还我们所需要的结果
             */
            ByteBuffer buffer = ByteBuffer.allocate(20);
            channel.read(buffer,0,buffer,new CompletionHandler<Integer, ByteBuffer>(){
                //回调函数成功完成
                @Override
                public void completed(Integer result, ByteBuffer attachment) {

                }

                //回调函数失败，未完成
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            })
        }
    }
}
