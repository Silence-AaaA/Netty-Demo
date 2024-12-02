package com.netty.NIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

public class FileChannelTransferTo {
    public static void main(String[] args) {
        //将一个channel当中文件的数据传给另外一个通道
        //需要注意的一点是，我们对应的channel是输入还是输出依靠的是我们的选择
        //输入还是输出可以看对象是输入流还是输出流
        //另外使用RandomAccessFile需要我们执行是读取还是写入 'rw' 为读写均可以
        try {
            FileChannel from = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("data1.txt").getChannel();
            /**
             * 其中包含三个参数。第一个代表起始位置，第二个代表当前文件的大小，第三个为目标文件
             *  这种方式类似于拷贝，但是这种效率会明显高，其底层使用的是零拷贝进行优化，所以
             *  比我们使用的输入输出流相比，效率更高
             */

            //但是实际上，这个channel传输数据是有限制的，最多为2G，如果拷贝超过2G的文件，那么2G之后的文件就不会再进行写入
            //拷贝之后的文件大小只会为2G
            //我们可以考虑动态设置对应的起始位置，从而解决
            //1.设置字节数总数
            long size = from.size();
            for (long lastByte = size; lastByte > 0  ;) {
                //其返还的是当前传输了多少的字节，所以我们将其在原本基础上修改即可
                lastByte -= from.transferTo((size - lastByte), size, to);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
