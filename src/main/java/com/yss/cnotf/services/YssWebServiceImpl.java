package com.yss.cnotf.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 11:00 2019/04/30
 */
@WebService(
        endpointInterface = "com.yss.cnotf.services.YssWebServiceI",
        portName = "yssWebServiceWSPort",
        serviceName = "yssWebServiceWSService",
        targetNamespace = "http://services.cnotf.yss.com")
public class YssWebServiceImpl implements YssWebServiceI {


    private final static Logger logger = LoggerFactory.getLogger(YssWebServiceImpl.class);
    /**
     * 接受报表拍照参数，并处理
     * @param biDateInfo
     * @return
     */
    @Override
    public String saveBiDate(BiDateInfo biDateInfo) {
        //参数都不会为空 省略判断的步骤
        //如果是 基金产品托管情况 和 其他产品托管情况 报表的话 拼接8个字段 其他报表拼接四个字段
        String paraAdd = "";
        if ("Rpt03".equals(biDateInfo.getBiName()) || "Rpt04".equals(biDateInfo.getBiName()) ) {
            paraAdd = biDateInfo.getStartYearPhotoDate()+"|"+biDateInfo.getStartYearAccDate()
                    +"|"+biDateInfo.getQuarterPhotoDate()+"|"+biDateInfo.getQuarterAccDate()
                    +"|"+biDateInfo.getLastQuarterPhotoDate()+"|"+biDateInfo.getLastQuarterAccDate()
                    +"|"+biDateInfo.getLastYearPhotoDate()+"|"+biDateInfo.getLastYearAccDate();
        } else {
            paraAdd = biDateInfo.getStartPhotoDate()+"|"+biDateInfo.getEndPhotoDate()
                    +"|"+biDateInfo.getStartAccountDate()+"|"+biDateInfo.getEndAccountDate();
        }
        //去掉日期拼接符
        paraAdd = paraAdd.replaceAll("-","");
        String tablename = biDateInfo.getBiName();
        logger.info("报表拍照参数=================：" + paraAdd+"=="+tablename);
        List<BiDateInfo> biDateInfos = new ArrayList<>();
        biDateInfos.add(biDateInfo);
        String returnFlag = "1";
        try {
            BiDateService.biService(paraAdd, tablename, biDateInfos);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }

        return returnFlag;
    }

    /**
     * 接受手工拍照参数，并处理
     * @param handDateInfoList
     * @return
     */
    @Override
    public String saveHandDate(List<HandDateInfo> handDateInfoList, String photoType) {

        String returnFlag = "1";
        try {
            HandDateService.handService(handDateInfoList, photoType);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }
        return returnFlag;
    }

    /**
     * 接受托管费信息
     * @param trusteeFeeInfo
     * @return
     */
    @Override
    public String saveTrusteeFeeData(List<TrusteeFeeInfo> trusteeFeeInfo) {
        String returnFlag = "1";
        try {
            TrusteeService.trusteeService(trusteeFeeInfo);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }
        return returnFlag;
    }

}


