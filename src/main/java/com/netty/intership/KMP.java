package com.netty.intership;

import static com.netty.intership.AsyncTest.list;

//相同的模式串可以匹配多个不同的字符串
public class KMP extends BaseClass implements Runnable{
    /**
     * 模式串
     */
    private String pat;
    /**
     * DP数组 存放状态
     */
    private int[][] dp;
    /**
     * 存放文本
     */
    private String stringTxt;


    public KMP(String pat) {
        this.pat = pat;
        modify();
    }

    public KMP(String pat,String stringTxt) {
        this.pat = pat;
        this.stringTxt = stringTxt;
        modify();
    }

    public String getPat() {
        return pat;
    }

    public void setPat(String pat) {
        this.pat = pat;
    }



    public String getStringTxt() {
        return stringTxt;
    }

    public void setStringTxt(String stringTxt) {
        this.stringTxt = stringTxt;
    }

    private void modify(){
        //初始化pat 模式串
        int M = pat.length();
        //初始化dp数组
        //这里256用来存储所有的字符 从而保证能够保证进行匹配
        dp = new int[M][40869];
        //设置初始化data base
        char c = pat.charAt(0);
        dp[0][pat.charAt(0)] = 1;
        //设置影子X 初始值为0
        int X = 0;
        for (int i = 1; i < M; i++) {
            for (int j = 0; j < 256; j++) {
                if (pat.charAt(i) == j) {
                    //两者相等的条件下，说明匹配到了，记录其为当前位置 + 1
                    dp[i][j] = i+1;
                }else {
                    //没有匹配到 将其记录为当前位置的上一个影子的位置
                    dp[i][j] = dp[X][j];
                }
            }
            //更新影子状态  需要知道：当前的状态 下一个元素是什么
            X = dp[X][pat.charAt(i)];
        }
    }

    public int search(String strTxt) {
        //保证如果找到了，那么就不再再次进行寻找
        if (!getResult()) {
            //开始搜寻
            long begin = System.nanoTime();
            int M = pat.length();
            int N = strTxt.length();
            //这个j代表的是我们当前字符匹配的状态，实际上并不是对应的索引
            int j = 0;
            //开始进行搜索
            //环状匹配 想要完全匹配至少为2倍数
            for (int i = 0; i < N; i++) {
                //按照索引进行搜索
                //dp当中需要明确两点：1.当前的状态 2.下一个字符是什么
                j = dp[j][strTxt.charAt(i)];
                incrementCallNumber();
                //脱离循环 达到长度相等的条件
                if (j == M) {
                    //返还当前pat第一次出现的索引位置
                    long end = System.nanoTime();
                    setResult(true);
                    setRunTime(end - begin);
                    return i - j + 1;
                }
            }
            long end = System.nanoTime();
            setRunTime(end - begin);
            return -1;
        }
        return -1;
    }


    //异步调用
    @Override
    public void run() {
        if (pat == null || stringTxt == null) {
            System.out.println("模式串以及文本不能为空！");
        }else {
            search(this.stringTxt);
        }
    }

}
