package com.netty.NIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Scatter {
    public static void main(String[] args) {
        //将一个文件当中的多个数据分开放入不同的Buffer当中
        try {
            FileInputStream fileInputStream = new FileInputStream("data.txt");
            FileChannel channel = fileInputStream.getChannel();
            //创建空间
            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(4);
            //写入数据,直接将当前通道的数据写入Buffer
            channel.read(new ByteBuffer[]{buffer1,buffer2,buffer3});
            //更改为读取操作，获取数据
            buffer2.flip();
            while (buffer2.hasRemaining()) {
                System.out.print((char)buffer2.get());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
