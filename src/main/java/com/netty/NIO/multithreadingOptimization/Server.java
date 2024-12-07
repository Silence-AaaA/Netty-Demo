package com.netty.NIO.multithreadingOptimization;

import org.apache.poi.xddf.usermodel.text.AutonumberScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

//使用多线程优化我们之前使用单线程写的内容
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        //将当前线程设置为主线程Boss , 主线程只有一个，只用来管理对应客户端与服务端之间的连接操作
        Thread.currentThread().setName("Boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress("127.0.0.1",8080));
        Selector selector = Selector.open();
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        //创建工人
        //优化为多个工人
        //自动获取当前电脑的最大线核心程数数量
        //当前多路复用均为同步
        System.out.println(Runtime.getRuntime().availableProcessors());
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        //设置计数器
        AtomicInteger ato = new AtomicInteger(0);
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    SocketChannel ss = ssc.accept();
                    ss.configureBlocking(false);
                    //这里在选择对应的选择器时候使用我们工人对应的选择器
                    log.debug("开始连接");
                    //这里明显没有进入到worker的选择器当中
                    //就是因为先后的执行顺序不一样
                    //如果worker的accept先执行，那么就会被阻塞，阻塞之后就会导致对应的BOSS的register无法正常进行 所以说我们要保证一定是 Boss的register
                    //在worker的前面才行,这样对应的worker的选择器的书简类型也注册好了，并且客户端也执行了对应操作，写操作，那么我们就可以开始进行读取数据了
                    //多个工人轮询执行
                    workers[ato.incrementAndGet()%workers.length].register(ss);
                    log.debug("连接结束");
                }
                else if (key.isReadable()){}
                else if (key.isWritable()){}
            }
        }
    }

    //创建内部类
    static class Worker implements Runnable{
        private String name;
        private Selector selector;
        private Thread thread;
        //用来保证对应注册的时候只能注册一次，为了避免注册一个就创建一个选择器
        private boolean isFirstUse = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) throws IOException {
            this.name = name;
        }

        //注册 但是需要保证注册且仅仅注册一次，为了避免对应创建过多的工人进行多次注册
        public void register(SocketChannel ss) throws IOException {
            if (!isFirstUse){
                this.selector = Selector.open();
                this.thread = new Thread(this,name);
                this.thread.start();
                isFirstUse = true;
            }
            //将对应的SocketChannel转变为对应的任务进行存储，这样就可以达到一个线程到另外一个线程之间信息的相互转化，这里使用安全队列！
            //将对应的执行操作放入队列
            //向队列添加任务，但是这里的任务没有执行
            queue.add(()->{
                try {
                    ss.register(this.selector,SelectionKey.OP_READ,null);
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            });
            //手动唤醒select()
            selector.wakeup();
        }

        @Override
        public void run() {
            //编写逻辑代码
            try {
                while (true) {
                    selector.select();
                    //获取任务
                    Runnable runnable = queue.poll();
                    //保证任务不为空
                    if (runnable != null) {
                        //执行任务代码
                        runnable.run();
                    }
                    //上述任务执行完成之后，第一遍是不会进入到这里的，因为第一遍对应的事件没有指定
                    //所以第一遍执行完之后，进入第二次循环，这个时候对应的事件类型也就设置好了，可以读取到对应的事件类型，从而执行下面的事件
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            //读取数据
                            log.debug("read---");
                            SocketChannel ss = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            ss.read(buffer);
                            System.out.println(buffer.toString());
                        } else if (key.isWritable()) {
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
