package com.yss.cnotf.services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 10:58 2019/04/30
 */
@WebService(
        name = "saveWebServiceWS",
        targetNamespace = "http://services.cnotf.yss.com"
)
public interface YssWebServiceI {

    @WebMethod
    public String saveBiDate(BiDateInfo biDateInfo);

    @WebMethod
    public String saveHandDate(List<HandDateInfo> handDateInfoList, String photoType);

    @WebMethod
    public String saveTrusteeFeeData(List<TrusteeFeeInfo> trusteeFeeInfo);


}
