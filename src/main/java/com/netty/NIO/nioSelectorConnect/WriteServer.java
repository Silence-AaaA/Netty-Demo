package com.netty.NIO.nioSelectorConnect;

import com.netty.NIO.files.WalkfiletreeVisitFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        //创建与选择器之间的连接
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //检测
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //移除当前KEY值
                iterator.remove();
                //判断类型
                if (key.isAcceptable()){
                    SocketChannel ss = serverSocketChannel.accept();
                    ss.configureBlocking(false);
                    //设置写操作
                    SelectionKey ssKey = ss.register(selector, 0, null);
                    ssKey.interestOps(SelectionKey.OP_READ);

                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < 1000000; i++) {
                        stringBuffer.append("W");
                    }

                    //简单获取数据
                    String string = stringBuffer.toString();
                    ByteBuffer byteBuffer = Charset.defaultCharset().encode(string.toString());

                    //代表写入的字节数量
                    int write = ss.write(byteBuffer);
                    System.out.println(write);

                    //如果说数据一次性没有读取完毕 结束，但是继承当前的ByteBuffer
                    if (byteBuffer.hasRemaining()){
                        //如果一次性没有写完，那么将其设置一个Writre的KEY，但是同时的，如果说当前的KEY还是需要与其他的KEY一起，那么就将其绑定在一起
                        //这里将写操作与读操作一起绑定
                        ssKey.interestOps(ssKey.interestOps() + SelectionKey.OP_WRITE);

                        //保存ByteBuffer为了之后继续读取
                        ssKey.attach(byteBuffer);
                    }

                } else if (key.isWritable()) {
                    //写操作
                    //获取上一次关连数据
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();

                    int write = 0;
                    try {
                        write = channel.write(buffer);
                    }catch (Exception e){
                        e.printStackTrace();
                        key.cancel();
                    }

                    if (write == 0){
                        //表明已经写完
                        key.cancel();
                    }

                    System.out.println(write);
                    //查看是否有剩余，没有剩余不再添加当前KEY
                    if (!buffer.hasRemaining()){
                        //将关联的数据清空，减少因为文件存储造成的资源压力
                        key.attach(null);
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                } else if (key.isReadable()) {
                    //获取通道
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //如果这里读取数据的量比较大，那么我们也应当分开读取，跟上面一样
                    int read = 0;
                    try {
                        read = socketChannel.read(buffer);
                        System.out.println(read);
                    }catch (Exception e){
                        //读取出错，那么直接取消
                        key.cancel();
                    }
                        System.out.println("read1 = " + read);

                        //read == 0表明数据读取完毕
                        if (read == 0){
                            //没有数据可以读取
                            //直接取消
                            key.cancel();
                        }

                }
            }
        }
    }
}
