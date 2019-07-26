package com.yss.cnotf.services;

import java.util.Date;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:26 2019/06/17
 */
public class BiDateInfo {

    private String startPhotoDate;

    private String endPhotoDate;

    private Date createDate;

    private String startAccountDate;

    private String endAccountDate;

    private String biName;

    private String operate;

    private String startYearPhotoDate;

    private String startYearAccDate;

    private String quarterPhotoDate;

    private String quarterAccDate;

    private String lastQuarterPhotoDate;

    private String lastQuarterAccDate;

    private String lastYearPhotoDate;

    private String lastYearAccDate;

    private Integer page;
    private Integer rows;
    private Integer total;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

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

    public String getStartYearPhotoDate() {
        return startYearPhotoDate;
    }

    public void setStartYearPhotoDate(String startYearPhotoDate) {
        this.startYearPhotoDate = startYearPhotoDate;
    }

    public String getStartYearAccDate() {
        return startYearAccDate;
    }

    public void setStartYearAccDate(String startYearAccDate) {
        this.startYearAccDate = startYearAccDate;
    }

    public String getQuarterPhotoDate() {
        return quarterPhotoDate;
    }

    public void setQuarterPhotoDate(String quarterPhotoDate) {
        this.quarterPhotoDate = quarterPhotoDate;
    }

    public String getQuarterAccDate() {
        return quarterAccDate;
    }

    public void setQuarterAccDate(String quarterAccDate) {
        this.quarterAccDate = quarterAccDate;
    }

    public String getLastQuarterPhotoDate() {
        return lastQuarterPhotoDate;
    }

    public void setLastQuarterPhotoDate(String lastQuarterPhotoDate) {
        this.lastQuarterPhotoDate = lastQuarterPhotoDate;
    }

    public String getLastQuarterAccDate() {
        return lastQuarterAccDate;
    }

    public void setLastQuarterAccDate(String lastQuarterAccDate) {
        this.lastQuarterAccDate = lastQuarterAccDate;
    }

    public String getLastYearPhotoDate() {
        return lastYearPhotoDate;
    }

    public void setLastYearPhotoDate(String lastYearPhotoDate) {
        this.lastYearPhotoDate = lastYearPhotoDate;
    }

    public String getLastYearAccDate() {
        return lastYearAccDate;
    }

    public void setLastYearAccDate(String lastYearAccDate) {
        this.lastYearAccDate = lastYearAccDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "BiDateInfo{" +
                "startPhotoDate='" + startPhotoDate + '\'' +
                ", endPhotoDate='" + endPhotoDate + '\'' +
                ", createDate=" + createDate +
                ", startAccountDate='" + startAccountDate + '\'' +
                ", endAccountDate='" + endAccountDate + '\'' +
                ", biName='" + biName + '\'' +
                ", operate='" + operate + '\'' +
                ", startYearPhotoDate='" + startYearPhotoDate + '\'' +
                ", startYearAccDate='" + startYearAccDate + '\'' +
                ", quarterPhotoDate='" + quarterPhotoDate + '\'' +
                ", quarterAccDate='" + quarterAccDate + '\'' +
                ", lastQuarterPhotoDate='" + lastQuarterPhotoDate + '\'' +
                ", lastQuarterAccDate='" + lastQuarterAccDate + '\'' +
                ", lastYearPhotoDate='" + lastYearPhotoDate + '\'' +
                ", lastYearAccDate='" + lastYearAccDate + '\'' +
                '}';
    }
}
