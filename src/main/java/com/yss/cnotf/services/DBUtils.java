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
    public static Connection newConnection(String dbtype,String dbHost,String dbPort,String dbInstranse,String dbUser,String dbPassword){
        Connection conn = null ;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbInstranse,dbUser,dbPassword);
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
