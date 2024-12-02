package com.netty.NIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Gather {
    public static void main(String[] args) throws FileNotFoundException {
        //集中数据读取，将多个的ByteBuffer的数据直接填充到对应的通道channel,聚集起来
        try {
            RandomAccessFile rw = new RandomAccessFile("data.txt", "rw");
            //获取通道
            FileChannel channel = rw.getChannel();
            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            ByteBuffer buffer2 = ByteBuffer.allocate(4);
            //设置数据
            buffer1.put(new byte[]{'1','2','3','4'});
            buffer2.put(new byte[]{'5','6','7','8'});
            //改为读取数据
            buffer2.flip();
            buffer1.flip();
            //将两者一起放入数组，之后写入到通道
            channel.write(new ByteBuffer[]{buffer1,buffer2});
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
