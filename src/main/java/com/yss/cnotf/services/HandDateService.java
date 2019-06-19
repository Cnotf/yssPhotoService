package com.yss.cnotf.services;

import java.sql.*;

/**
 * @Author: cnotf
 * @Description: 手工拍照数据处理
 * @Date: Create in 16:42 2019/05/24
 */
public class HandDateService {

    public static void handService (String paraAdd) throws Exception{
        HandDateService handDateService = new HandDateService();
        handDateService.insertIntoDataofMysql(paraAdd);
    }

    private void insertIntoDataofMysql(String paraAdd) throws Exception {
        Connection conn = DBUtils.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://10.7.53.28:3306/mpetl", "mpetl", "mpetl");
        deleteHandData(conn,paraAdd);
        insertIntoHandTable(conn,paraAdd);
        conn.close();
    }


    /**
     * 删除手工拍照重复数据
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void deleteHandData(Connection conn, String pataAdd) throws Exception{
        PreparedStatement stat = conn.prepareStatement("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE1= '" + pataAdd + "' AND DATA_DT='" + MyDateUtils.getDateToString() + "' AND SOURCE_FLAG='A'");
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
    private void insertIntoHandTable (Connection conn, String pataAdd) throws Exception{
        Statement stat = null;
        stat = conn.createStatement();
        try {
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+MyDateUtils.getDateToString()+"','"+pataAdd+"','','A','0','M01_INT_GRP_INFO_IMG','');");
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+MyDateUtils.getDateToString()+"','"+pataAdd+"','','A','0','M01_PRD_STAT_INFO','');");
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+MyDateUtils.getDateToString()+"','"+pataAdd+"','','A','0','M01_TS_CORE_DATA_IMG','');");
            stat.executeBatch();
        } finally {
            stat.close();
        }
    }

}
