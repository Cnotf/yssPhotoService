package com.yss.cnotf.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 09:40 2019/05/14
 */
public class DBUtils {

    public static Connection getConnection(String driver,String url, String username, String password){
        Connection conn = null ;
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, password);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
