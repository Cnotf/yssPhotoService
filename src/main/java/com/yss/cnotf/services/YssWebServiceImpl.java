package com.yss.cnotf.services;

import javax.jws.WebService;
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

    /**
     * 接受报表拍照参数，并处理
     * @param biDateInfo
     * @return
     */
    @Override
    public String saveBiDate(BiDateInfo biDateInfo) {
        //参数都不会为空 省略判断的步骤
        String paraAdd = biDateInfo.getStartPhotoDate()+"|"+biDateInfo.getEndPhotoDate()
                +"|"+biDateInfo.getStartAccountDate()+"|"+biDateInfo.getStartAccountDate();
        String tablename = biDateInfo.getBiName();
        System.out.println("报表拍照：" + paraAdd+"=="+tablename);
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
     * @param handDateInfo
     * @return
     */
    @Override
    public String saveHandDate(HandDateInfo handDateInfo) {
        //参数都不会为空 省略判断的步骤
        String paraAdd = handDateInfo.getBeginHandDate()+"|"+handDateInfo.getEndHandDate();
        System.out.println("手工拍照："+paraAdd);
        String returnFlag = "1";
        try {
            HandDateService.handService(paraAdd);
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


