package com.yss.cnotf.services;

import java.math.BigDecimal;

/**
 * @Author: cnotf
 * @Description: 托管费信息
 * @Date: Create in 13:38 2019/06/17
 */
public class TrusteeFeeInfo {


    private Long id;
    /**
     * 投资组合代码
     */
    private String intGrpCd;

    /**
     * 付款日期
     */
    private String pymDt;
    /**
     * 付款账号
     */
    private String pymAccNo;

    /**
     * 投资组合名称
     */
    private String intGrpNm;

    /**
     * 金额
     */
    private BigDecimal amt;

    /**
     * 收款账号
     */
    private String rcptAccNo;

    /**
     * 来源类型
     */
    private String scrType;

    /**
     * 是否关联
     */
    private String isRltv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntGrpCd() {
        return intGrpCd;
    }

    public void setIntGrpCd(String intGrpCd) {
        this.intGrpCd = intGrpCd;
    }

    public String getPymDt() {
        return pymDt;
    }

    public void setPymDt(String pymDt) {
        this.pymDt = pymDt;
    }

    public String getPymAccNo() {
        return pymAccNo;
    }

    public void setPymAccNo(String pymAccNo) {
        this.pymAccNo = pymAccNo;
    }

    public String getIntGrpNm() {
        return intGrpNm;
    }

    public void setIntGrpNm(String intGrpNm) {
        this.intGrpNm = intGrpNm;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getRcptAccNo() {
        return rcptAccNo;
    }

    public void setRcptAccNo(String rcptAccNo) {
        this.rcptAccNo = rcptAccNo;
    }

    public String getScrType() {
        return scrType;
    }

    public void setScrType(String scrType) {
        this.scrType = scrType;
    }

    public String getIsRltv() {
        return isRltv;
    }

    public void setIsRltv(String isRltv) {
        this.isRltv = isRltv;
    }

    @Override
    public String toString() {
        return "TrusteeFeeInfo{" +
                "intGrpCd='" + intGrpCd + '\'' +
                ", pymDt='" + pymDt + '\'' +
                ", pymAccNo='" + pymAccNo + '\'' +
                ", intGrpNm='" + intGrpNm + '\'' +
                ", amt=" + amt +
                ", rcptAccNo='" + rcptAccNo + '\'' +
                ", scrType='" + scrType + '\'' +
                ", isRltv='" + isRltv + '\'' +
                '}';
    }
}
