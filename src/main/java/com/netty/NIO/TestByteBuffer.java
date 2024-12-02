package com.netty.NIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {
    public static void main(String[] args) {
        //FileChannel
        //使用NIO的方式获取读取文件
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //设置缓冲区 并且为此缓冲区分配空间，大小为10
            System.out.println(channel);
            ByteBuffer allocate = ByteBuffer.allocate(10);
            //从channel读取，就是向缓冲区写
            channel.read(allocate);
            //打印缓冲区内部的内容
            allocate.flip(); //将缓冲区操作切换为读取
            while (allocate.hasRemaining()) { //判断缓冲区域当中是否还存在其他数据
                byte b = allocate.get();
                System.out.println((char) b);
            }
        } catch (IOException e) {
        }
    }
}
