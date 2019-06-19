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
}
