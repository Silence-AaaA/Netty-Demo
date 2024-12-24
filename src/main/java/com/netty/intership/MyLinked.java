package com.netty.intership;

public class MyLinked<T> {
    private T[] element;
    private int size;
    private int rear = 0;
    private int tail = 0;
    private final int DEFAULT_CAPACITY = 50;
    public MyLinked() {
        element = (T[]) new Object[DEFAULT_CAPACITY];
        size = DEFAULT_CAPACITY;
    }

    public MyLinked(int size) {
        element = (T[]) new Object[size];
        this.size = size;
    }

    /**
     * 添加元素
     */
    public void add(T t){
        element[tail] = t;
        tail++;
    }

    public T pop(){
        T t = element[rear];
        rear++;
        return t;
    }

    public T[] peekAll(){
        //获取所有的数据
        T[] temp = (T[]) new Object[tail - rear];
        for (int i = rear; i < tail; i++) {
            temp[i] = element[i];
        }
        return temp;
    }
}
