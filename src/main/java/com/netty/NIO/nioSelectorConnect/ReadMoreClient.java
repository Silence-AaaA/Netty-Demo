package com.netty.NIO.nioSelectorConnect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReadMoreClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        //设置要向服务端传输的文件的大小
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 1000000; i++) {
            stringBuffer.append("W");
        }

        ByteBuffer encode = Charset.defaultCharset().encode(CharBuffer.wrap(stringBuffer));
        socketChannel.write(encode);
        System.out.println("Client write success");
    }
}
