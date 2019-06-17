package com.yss.cnotf.services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import java.util.Map;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 10:58 2019/04/30
 */
@WebService(
        name = "yssWebServiceWS",
        targetNamespace = "http://com.yss.hello.service"
)
public interface yssWebServiceI {

    @WebMethod
    public String getBiDate(BiDateInfo biDateInfo);

    @WebMethod
    public String getHandDate(HandDateInfo handDateInfo);

    @WebMethod
    public String saveTrusteeFeeData(TrusteeFeeInfo trusteeFeeInfo);

    @WebMethod
    public List<Map<String, Object>> queryTrusteeFeeData();
}
