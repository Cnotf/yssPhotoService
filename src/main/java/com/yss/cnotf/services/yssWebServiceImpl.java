package com.yss.cnotf.services;

import javax.jws.WebService;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 11:00 2019/04/30
 */
@WebService(
        endpointInterface = "com.yss.cnotf.services.yssWebServiceI",
        portName = "yssWebServiceWSPort",
        serviceName = "yssWebServiceWSService",
        targetNamespace = "http://com.yss.hello.service")
public class yssWebServiceImpl implements yssWebServiceI {

    /**
     * 接受报表拍照参数，并处理
     * @param startPhotoDate
     * @param endPhotoDate
     * @param startAccountDate
     * @param endAccountDate
     * @return
     */
    @Override
    public String getBiDate(String startPhotoDate, String endPhotoDate,
                            String startAccountDate, String endAccountDate,
                            String biName) {
        String paraAdd = startPhotoDate+"|"+endPhotoDate+"|"+startAccountDate+"|"+startAccountDate;
        String tablename = biName;
        String returnFlag = "1";
        try {
            BiDateService.biService(paraAdd, tablename);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }

        return returnFlag;
    }

    /**
     * 接受手工拍照参数，并处理
     * @param beginHandDate
     * @param endHandDate
     * @return
     */
    @Override
    public String getHandDate(String beginHandDate, String endHandDate) {
        String paraAdd = beginHandDate+"|"+endHandDate;
        String returnFlag = "1";
        try {
            HandDateService.handService(paraAdd);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }
        return returnFlag;
    }
}


