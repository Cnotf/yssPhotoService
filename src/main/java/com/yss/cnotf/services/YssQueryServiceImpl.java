package com.yss.cnotf.services;

import javax.jws.WebService;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 09:40 2019/06/18
 */
@WebService(
        endpointInterface = "com.yss.cnotf.services.YssQueryServiceImpl",
        portName = "yssQueryServiceWSPort",
        serviceName = "yssQueryServiceWSService",
        targetNamespace = "http://services.cnotf.yss.com")
public class YssQueryServiceImpl implements YssQueryServiceI {

    /**
     * 查询托管费信息
     * @return
     */
    @Override
    public List<TrusteeFeeInfo> queryTrusteeFeeData(TrusteeFeeInfo trusteeFeeInfo) {
        List<TrusteeFeeInfo> listMap= null;
        try {
            listMap = TrusteeService.queryTrusteeData(trusteeFeeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }


    /**
     * 查询拍照信息
     * @param handDateInfo
     * @return
     */
    @Override
    public List<HandDateInfo> queryHandPhotoList(HandDateInfo handDateInfo){
        List<HandDateInfo> listMap= null;
        HandDateService handDateService = new HandDateService();
        try {
            listMap = handDateService.queryPhotoDataList(handDateInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }


    /**
     * 查询报表信息
     * @param biDateInfo
     * @return
     */
    @Override
    public List<BiDateInfo> queryBiList(BiDateInfo biDateInfo){
        List<BiDateInfo> listMap= null;
        BiDateService biDateService = new BiDateService();
        try {
            listMap = biDateService.queryBiDataList(biDateInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

    @Override
    public Integer deleteHandPhotoData(List<HandDateInfo> handDateInfoList) {
        HandDateService handDateService = new HandDateService();
        Integer returnMsg = 0;
        try {
            handDateService.deleteHandPhotoData(handDateInfoList);
            returnMsg = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMsg;
    }

    @Override
    public Integer saveHandPhotoData(List<HandDateInfo> handDateInfoList) {
        HandDateService handDateService = new HandDateService();
        Integer returnMsg = 0;
        try {
            handDateService.savePhotoDataList(handDateInfoList);
            returnMsg = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMsg;
    }

}
