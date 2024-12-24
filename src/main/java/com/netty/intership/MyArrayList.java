package com.netty.intership;

import java.util.concurrent.atomic.AtomicInteger;

public class MyArrayList<T> {
    private T[] element;

    private int size;

    private MyLinked myLinked;

    private int elementIndex = 0;

    private Double maxNum = 0.00;

    private int[][] map;

    //数组默认值大小
    private final int DEFAULT_CAPACITY = 15;
    //元素扩展倍数
    private final double EXPANSION_FACTOR = 1.5;

    public MyArrayList() {
        element = (T[]) new Object[DEFAULT_CAPACITY];
        size = DEFAULT_CAPACITY;
    }

    //自动设置数组大小
    public MyArrayList(int capacity) {
        element = (T[]) new Object[capacity];
        size = capacity;
    }

    public Double getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Double maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * 遍历元素
     */
    public void traverse(){
        for(int i = 0; i < elementIndex; i++){
            System.out.println(element[i]);
        }
    }

    /**
     * 生成有关两者时间之之比的图像，方便更加直观的进行观察
     */
    public void createTimeRateMap(){
        //Y方向上，一个为0.1的比率  最多为rate = 2 也就是20个
        //Y方向上，如果倍率比较小，那么详细展示，一个为0.05的比率  最多为rate = 1 也就是20个
        //自动化判断，看对应的最大比率是多少
        System.out.println("🔝 (KMP-TIME/BF-TIME)");
        calculateLocation();
        //设置MAP 通过此获得对应数据出现次数，保证每一行即使有多个重复存在，也能够消除
        map = new int[20][1];
        for (int i = 1; i <=20; i++) {
            System.out.print("||    ");
            Object[] objects = myLinked.peekAll();
            for (int i1 = 0; i1 < objects.length; i1++) {
                Double[] object = (Double[]) objects[i1];
                if (object[0] == 20-i){
                    //打印空格，设置间隔
                    int stage = (int) Math.round(object[0]);
                    for (int i2 = 0; i2 < i1-map[stage][0]; i2++) {
                        //16空间
                        System.out.print("                ");
                    }
                    //保存到图当中
                    map[stage][0] = i1;
                    //打印数据
                    System.out.print(i1+1+":("+String.format("%.2f",object[1])+")");
                }
            }
            System.out.println();
        }
        System.out.print("O =============");


        //X方向上
        for (int i1 = 0; i1 < elementIndex; i1++) {
            //生成图像
            System.out.print("===============");
        }
        System.out.println("=========🔜(从左向右按顺序输出)");
    }

    private void calculateLocation() {
        myLinked = new MyLinked<Double[]>();
        //两个数字记录，第一个记录阶层，第二个记录对应的值
        Double[] Locations;
        //Y方向上，一个为0.1的比率  最多为rate = 5 也就是50个
        //Y方向上，如果倍率比较小，那么详细展示，一个为0.05的比率  最多为rate = 1 也就是50个
        //设置扩展程度
        Double maxRate = getMaxNum()>1?0.1:0.05;
        for (int i1 = 0; i1 < elementIndex; i1++) {
            Locations = new Double[2];
            Double rate = (Double) element[i1];
            double v = Double.parseDouble(String.format("%.2f",rate % maxRate));
            //综合考虑四舍五入 对0.33 0.05精度向上取  0.1精度向下取
            rate = maxRate == 0.1?(v>=0.05?rate-v+0.1:rate-v):(v>=0.03?rate-v+0.05:rate-v);
            String.format("%.2f",rate);
            //计算应当位于第第几层级的位置
            Double stage = (double) Math.round(rate/maxRate);
            Locations[0] = stage;
            Locations[1] = rate;
            myLinked.add(Locations);
        }
    }

    /**
     * 添加元素
     * @param element
     */
    public void add(T element) {
        if (elementIndex+1 == size){
            //长度相等，扩展
            extend();
        }
        //直接添加
        if(element instanceof Double){
            if(Double.parseDouble(String.valueOf(element)) > maxNum) setMaxNum((Double) element);
        }
        this.element[elementIndex] = element;
        elementIndex++;
    }

    /**
     * 弹出元素
     */
    public T pop() {
        if (elementIndex == 0){
            return null;
        }
        elementIndex--;
        return element[elementIndex];
    }

    /**
     * peek
     */
    public T peek(){
        if (elementIndex == 0){
            return null;
        }
        return element[elementIndex];
    }

    /**
     * 扩展数组
     */
    private void extend(){
        //扩展当前数组
        T[] NEW = (T[]) new Object[(int) (this.size*EXPANSION_FACTOR)];
        this.size = (int) (this.size*EXPANSION_FACTOR);
        for (int i = 0; i < this.element.length; i++) {
            NEW[i] = element[i];
        }
        this.element = NEW;
    }
}
