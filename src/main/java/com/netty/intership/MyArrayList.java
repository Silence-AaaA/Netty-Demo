package com.netty.intership;

public class MyArrayList<T> {
    private T[] element;

    private int size;

    private int elementIndex = 0;

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

    /**
     * 遍历元素
     */
    public void traverse(){
        for(int i = 0; i < elementIndex; i++){
            System.out.println(element[i]);
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
