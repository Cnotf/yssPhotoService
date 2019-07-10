package com.yss.cnotf.services;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 16:31 2019/05/24
 */
public class DateUtils {

    public static String getDateToString () {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date).toString();
    }
}
