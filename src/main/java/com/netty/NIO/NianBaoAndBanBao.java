package com.netty.NIO;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class NianBaoAndBanBao {
    public static void main(String[] args) {
        //粘包和半包
        //实际开发当中会遇到以下问题
        /**
         * 一般来说，数据在传输的时候，都是将好几条数据结合在一起进行发送，这样提高了发送的效率
         * 但是这样发送消息，就可能会让对方在接收消息的时候产生一些另类的现象
         * 发送：Hello\nHow's it going today?/n
         *      I'm very happy,and you?
         * 接收：Hello,how's it going today?/nI'm v
         *      ery happy,and you?
         * 这就造成了粘包和半包
         * 粘包指的就是，对应的数据传输的时候连接到了一起，变成了一条数据
         * 半包指的就是，对应的数据没有一起展示出来，因为段落分开展示，这也是不对的
         * 将其按照/n分割回复
         */
        //设置buffer充当传出过来的数据
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.put("Hello\nHow's it going today?\nI'm v".getBytes(Charset.defaultCharset()));
        splite(buffer);
        buffer.put("ery happy,and you?".getBytes(Charset.defaultCharset()));
        splite(buffer);
    }

    private static void splite(ByteBuffer buffer) {
        buffer.flip();
        while (buffer.hasRemaining()) {
            //判断数据
            char b = (char)buffer.get();
            //一个换行符就是一个字符
            if (b == '\n'){
                //换行操作
                System.out.println();
            }else {
                System.out.print(b);
            }
        }
        //清空数据
        buffer.clear();
    }
}
