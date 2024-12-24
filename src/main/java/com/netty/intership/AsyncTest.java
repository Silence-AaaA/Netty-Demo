package com.netty.intership;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncTest {
    public static MyArrayList<String> list = new MyArrayList();
    public static MyArrayList<Double> V_Map = new MyArrayList();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入要处理的模式串");
        System.out.println("请输入要处理的文本");
        String virusStr = in.next();
        String peoStr = in.next();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        while(!virusStr.equals("0") && !peoStr.equals("0")){
            atomicInteger.incrementAndGet();
            /**
             * 输入字符串前置处理
             */
            int virLength = virusStr.length();
            //保存当前输入的数据，方便之后统计使用
            String nowVirusStr = virusStr;
            String nowPeoStr = peoStr;
            //将病毒环状DNA设置为环
            virusStr+=virusStr;
            //将人的DNA拼接为一个伪环状
            peoStr+=peoStr;
            KMP kmp = null;
            BF bf = null;
            for (int i = 0; i < virLength; i++) {
                String subVir = virusStr.substring(i, virLength);
                /**
                 * 启动KMP BF算法
                 */
                if (kmp == null || !kmp.getResult())  {
                    kmp = new KMP(subVir, peoStr);
                    kmp.run();
                }
                if (bf == null || !bf.getResult()) {
                    bf = new BF(subVir,peoStr);
                    bf.run();
                }
            }
            list.add(atomicInteger + ":针对模式串:"+nowVirusStr+" 以及文本:"+nowPeoStr+" 此次使用KMP运行的的结果为:" + (kmp.getResult()?"YES":"NO") + "，花费了" + kmp.getRunTime() + "纳秒, 调用了" +kmp.getCallNumber()+"次");
            list.add(atomicInteger + ":针对模式串:"+nowVirusStr+" 以及文本:"+nowPeoStr+" 此次使用BF运行的的结果为:"+(bf.getResult()?"YES":"NO") + "，花费了" + bf.getRunTime() + "纳秒, 调用了" +bf.getCallNumber()+"次");

            System.out.println("请输入要处理的模式串");
            System.out.println("请输入要处理的文本");
            virusStr = in.next();
            peoStr = in.next();
        }
        list.traverse();
    }
}
