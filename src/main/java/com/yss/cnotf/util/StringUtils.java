package com.yss.cnotf.util;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 15:37 2019/07/09
 */
public class StringUtils {

    /**
     * 将字符串的首字母转大写
     * @param str 需要转换的字符串
     * @return
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
