package com.netty.Netty.mainPart.eventLoopGroup;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class exe1 {
    public static void main(String[] args) {
        //使用一些eventLoop的基本操作
        //一个eventLoopGroup对象实际上包含了多个的eventLoop对象，其空参的对象个数默认为你电脑的核数*2
        //1.创建eventLoop对象
        EventLoopGroup group = new NioEventLoopGroup(2); // 这种实现方式常用于IO数据传输 也可以进行其他正常任务的执行以及定时任务
        //EventLoopGroup group2 = new DefaultEventLoopGroup(); //这种方式就是用于处理常规任务的，不处理IO任务

        //2.获取其中的EventLoop对象
        //这里获取的对象是以轮询的方式进行的
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //3.执行普通任务
        group.next().execute(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("这是普通任务！我在执行的时候是异步的！");
        });
        log.debug("main");

        //4.执行定时任务 这里的定时任务同时也是一个异步进行的
        group.scheduleAtFixedRate(()->{
            log.debug("这里是定时任务，我将会按照一定的频率，一定的时间执行！");
        },0,1, TimeUnit.SECONDS);

        log.debug("结束");
    }
}
