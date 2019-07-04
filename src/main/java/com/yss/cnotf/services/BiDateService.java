package com.yss.cnotf.services;

import com.yss.cnotf.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Author: cnotf
 * @Description:报表拍照处理流程
 * @Date: Create in 17:00 2019/05/24
 */
public class BiDateService {

    public static void biService (String paraAdd, String tablename) throws Exception{
        BiDateService biDateService = new BiDateService();
        biDateService.insertIntoDataofMysql(paraAdd, tablename);
    }

    private void insertIntoDataofMysql(String paraAdd, String tablename) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
//        Connection conn = DBUtils.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://10.7.53.28:3306/mpetl", "mpetl", "mpetl");
        deleteBiData(conn,paraAdd,tablename);
        insertIntoBiTable(conn,paraAdd,tablename);
        conn.close();
    }


    /**
     * 删除手工拍照重复数据
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void deleteBiData(Connection conn, String pataAdd, String tablename) throws Exception{
        PreparedStatement stat = conn.prepareStatement("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE2= '" + pataAdd + "' AND DATA_DT='" + MyDateUtils.getDateToString() + "' AND SOURCE_FLAG='B' and COLUMN1 = '" + tablename + "'");
        try {
            stat.execute();
        } finally {
            stat.close();
        }
    }

    /**
     * 插入数据库
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void insertIntoBiTable (Connection conn, String pataAdd, String tablename) throws Exception{
        PreparedStatement stat = conn.prepareStatement("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+ MyDateUtils.getDateToString() + "','','" + pataAdd + "','B','0','" + tablename + "','')");
        try {
            stat.execute();
        } finally {
            stat.close();
        }
    }
}
