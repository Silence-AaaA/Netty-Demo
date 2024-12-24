package com.netty.intership;

public class BaseClass {
    /**
     * 查找时间
     */
    private Long runTime = 1L;

    /**
     * 存放是否匹配到相应内容
     */
    private Boolean result = false;

    /**
     * 查询次数
     */
    private int callNumber = 0;

    public void incrementCallNumber() {
        callNumber++;
    }

    public int getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(int callNumber) {
        this.callNumber = callNumber;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
