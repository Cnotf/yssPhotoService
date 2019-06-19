package com.yss.cnotf.services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 09:37 2019/06/18
 */
@WebService(
        name = "queryServiceWS",
        targetNamespace = "http://services.cnotf.yss.com"
)
public interface YssQueryServiceI {

    /**
     * 查询托管费信息
     * @return
     */
    @WebMethod
    public List<TrusteeFeeInfo> queryTrusteeFeeData(TrusteeFeeInfo trusteeFeeInfo);
}
