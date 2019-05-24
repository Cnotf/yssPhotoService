package com.yss.cnotf.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

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
    public String getBiDate(String startPhotoDate, String endPhotoDate, String startAccountDate, String endAccountDate, String biName);

    @WebMethod
    public String getHandDate(String beginHandDate, String endHandDate);
}
