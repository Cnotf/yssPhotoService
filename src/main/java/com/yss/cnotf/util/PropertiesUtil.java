package com.yss.cnotf.util;

import java.io.*;
import java.util.Properties;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 09:18 2019/05/01
 */
public class PropertiesUtil  {
    private String properiesName = "";

    public PropertiesUtil() {

    }
    public PropertiesUtil(String fileName) {
        this.properiesName = fileName;
    }
    public String readProperty(String key) {
        String value = "";
        InputStream is = null;
        String path = "";
        try {
            path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            is = new FileInputStream(path + "/" + properiesName);
            //下面的方法是从内存中加载配置文件，第一次加载之后会缓存，修改之后还是获取以前的内容 ，所以需要用上面的方式
//            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(
//                    properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        String path = "";

        try {
            path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            is = new FileInputStream(path + "/" + properiesName);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(properiesName);
            p.load(is);
            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
