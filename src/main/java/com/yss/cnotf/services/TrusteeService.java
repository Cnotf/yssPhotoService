package com.yss.cnotf.services;

import java.sql.*;
import java.util.*;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:43 2019/06/17
 */
public class TrusteeService {

    public static void trusteeService(TrusteeFeeInfo trusteeFeeInfo) throws Exception{
        TrusteeService trusteeService = new TrusteeService();
        trusteeService.insertIntoDatao4Hive(trusteeFeeInfo);
    }
    public static List<Map<String, Object>>  queryTrusteeData() throws Exception{
        TrusteeService trusteeService = new TrusteeService();
        Connection conn = DBUtils.mysqlConnection("MYSQL","127.0.0.1","3306","vuemail","root","cnotfGao2018");
        List<Map<String, Object>> list = trusteeService.queryTrusteeTable(conn);
        conn.close();
        return list;
    }

    private void insertIntoDatao4Hive(TrusteeFeeInfo trusteeFeeInfo) throws Exception {
//        Connection conn = DBUtils.mysqlConnection("MYSQL","10.7.53.28","3306","mpetl","mpetl","mpetl");
        Connection conn = DBUtils.mysqlConnection("MYSQL","127.0.0.1","3306","vuemail","root","cnotfGao2018");
        insertIntoTrusteeTable(conn,trusteeFeeInfo);
        conn.close();
    }


    /**
     * 插入数据库
     * @param conn
     * @param trusteeFeeInfo
     * @throws Exception
     */
    private void insertIntoTrusteeTable (Connection conn, TrusteeFeeInfo trusteeFeeInfo) throws Exception{
//        PreparedStatement stat = conn.prepareStatement("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+ MyDateUtils.getDateToString() + "','','" + pataAdd + "','B','0','" + tablename + "','')");
        PreparedStatement stat = conn.prepareStatement("INSERT into m01_mnul_rltv_tsfe_data SELECT Pym_Dt," + trusteeFeeInfo.getIntGrpCd() + "Pym_Acc_No," + trusteeFeeInfo.getIntGrpNm() + "Amt,Rcpt_Acc_No,'4','1' from m01_mnul_rltv_tsfe_data_copy");
        try {
            stat.execute();
        } finally {
            stat.close();
        }
    }
    /**
     * 插入数据库
     * @param conn
     * @throws Exception
     */
    private List<Map<String, Object>>  queryTrusteeTable(Connection conn) throws Exception{
//        PreparedStatement stat = conn.prepareStatement("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+ MyDateUtils.getDateToString() + "','','" + pataAdd + "','B','0','" + tablename + "','')");
//        List<TrusteeFeeInfo> rsList = new ArrayList<TrusteeFeeInfo>();
        ArrayList<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rsMap = null;

        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement("select Pym_Dt,Int_Grp_cd,Pym_Acc_No,Int_Grp_Nm,Amt,Rcpt_Acc_No,Src_Type,Is_Rltv from m01_mnul_rltv_tsfe_data_copy");
            rs = stat.executeQuery();
            // 取得数据库的列名
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()) {
                rsMap = new HashMap<String, Object>(numberOfColumns);
                for (int i = 1; i < numberOfColumns + 1; i++) {
                    rsMap.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                rsList.add(rsMap);
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
                if (stat != null){
                    stat.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return rsList;
    }
}
