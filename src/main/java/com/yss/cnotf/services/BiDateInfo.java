package com.yss.cnotf.services;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:26 2019/06/17
 */
public class BiDateInfo {

    private String startPhotoDate;

    private String endPhotoDate;

    private String startAccountDate;

    private String endAccountDate;

    private String biName;

    public String getStartPhotoDate() {
        return startPhotoDate;
    }

    public void setStartPhotoDate(String startPhotoDate) {
        this.startPhotoDate = startPhotoDate;
    }

    public String getEndPhotoDate() {
        return endPhotoDate;
    }

    public void setEndPhotoDate(String endPhotoDate) {
        this.endPhotoDate = endPhotoDate;
    }

    public String getStartAccountDate() {
        return startAccountDate;
    }

    public void setStartAccountDate(String startAccountDate) {
        this.startAccountDate = startAccountDate;
    }

    public String getEndAccountDate() {
        return endAccountDate;
    }

    public void setEndAccountDate(String endAccountDate) {
        this.endAccountDate = endAccountDate;
    }

    public String getBiName() {
        return biName;
    }

    public void setBiName(String biName) {
        this.biName = biName;
    }

    @Override
    public String toString() {
        return "BiDateInfo{" +
                "startPhotoDate='" + startPhotoDate + '\'' +
                ", endPhotoDate='" + endPhotoDate + '\'' +
                ", startAccountDate='" + startAccountDate + '\'' +
                ", endAccountDate='" + endAccountDate + '\'' +
                ", biName='" + biName + '\'' +
                '}';
    }


}
