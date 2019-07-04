package com.yss.cnotf.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 10:33 2019/07/04
 */
public class Test {

    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        logger.info("开始。。。。。");
        List<TrusteeFeeInfo> list = new ArrayList<>();
        TrusteeFeeInfo trusteeFeeInfo = new TrusteeFeeInfo();
        trusteeFeeInfo.setId(BigInteger.valueOf(3L));
        trusteeFeeInfo.setIntGrpCd("dddd");
        trusteeFeeInfo.setIntGrpNm("cnotf");
        list.add(trusteeFeeInfo);
        YssWebServiceImpl yssWebService = new YssWebServiceImpl();
        yssWebService.saveTrusteeFeeData(list);
    }
}
