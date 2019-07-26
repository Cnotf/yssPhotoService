package com.yss.cnotf.services;

import java.util.Date;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:27 2019/06/17
 */
public class HandDateInfo {

    private Long id;

    private String photoOperationDate;

    private String photoDate;

    private Date createDate;

    private String photoStatus;

    private String deleteFlag;

    private String photoType;

    private Integer page;
    private Integer rows;
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoOperationDate() {
        return photoOperationDate;
    }

    public void setPhotoOperationDate(String photoOperationDate) {
        this.photoOperationDate = photoOperationDate;
    }

    public String getPhotoDate() {
        return photoDate;
    }

    public void setPhotoDate(String photoDate) {
        this.photoDate = photoDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(String photoStatus) {
        this.photoStatus = photoStatus;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    @Override
    public String toString() {
        return "HandDateInfo{" +
                "id=" + id +
                ", photoOperationDate='" + photoOperationDate + '\'' +
                ", photoDate='" + photoDate + '\'' +
                ", createDate=" + createDate +
                ", photoStatus='" + photoStatus + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                ", photoType='" + photoType + '\'' +
                '}';
    }
}
