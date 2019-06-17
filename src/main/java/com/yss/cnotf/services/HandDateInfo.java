package com.yss.cnotf.services;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:27 2019/06/17
 */
public class HandDateInfo {

    private String beginHandDate;

    private String endHandDate;

    public String getBeginHandDate() {
        return beginHandDate;
    }

    public void setBeginHandDate(String beginHandDate) {
        this.beginHandDate = beginHandDate;
    }

    public String getEndHandDate() {
        return endHandDate;
    }

    public void setEndHandDate(String endHandDate) {
        this.endHandDate = endHandDate;
    }

    @Override
    public String toString() {
        return "HandDateInfo{" +
                "beginHandDate='" + beginHandDate + '\'' +
                ", endHandDate='" + endHandDate + '\'' +
                '}';
    }
}
