package com.yss.cnotf.services;

import com.yss.cnotf.util.PropertiesUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:43 2019/06/17
 */
public class TrusteeService {

    /**
     * 该类提供的查询的静态入口方法
     * @param trusteeFeeInfo
     * @throws Exception
     */
    public static void trusteeService(List<TrusteeFeeInfo> trusteeFeeInfo) throws Exception{
        TrusteeService trusteeService = new TrusteeService();
        trusteeService.insertIntoData(trusteeFeeInfo);
    }

    /**
     * 该类提供的保存的静态入口方法
     * @param trusteeFeeInfo
     * @return
     * @throws Exception
     */
    public static List<TrusteeFeeInfo>  queryTrusteeData(TrusteeFeeInfo trusteeFeeInfo) throws Exception{
        TrusteeService trusteeService = new TrusteeService();
        return trusteeService.queryTrusteeFeeTable(trusteeFeeInfo,0);
    }


    /**
     * 查询mysql中托管数据
     * @param trusteeFee
     * @param queryType =0时：为页面请求的查询 =1时：为插入hive提交查询所有is_rltv为1的数据
     * @return
     * @throws Exception
     */
    private List<TrusteeFeeInfo> queryTrusteeFeeTable (TrusteeFeeInfo trusteeFee, int queryType) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("ipport.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        List<TrusteeFeeInfo> rsList = new ArrayList<TrusteeFeeInfo>();
        PreparedStatement stat = null;
        ResultSet rs = null;
        String queryStr = "select Pym_Dt,Int_Grp_cd,Pym_Acc_No,Int_Grp_Nm,Amt,Rcpt_Acc_No,id,Is_Rltv from  m01_mnul_rltv_tsfe_data where Is_Rltv = '"+ trusteeFee.getIsRltv() + "' ";
        if (queryType == 0) {
            queryStr = queryStr +"limit 0,50";
        }

        try {
            stat = conn.prepareStatement(queryStr);
            rs = stat.executeQuery();
            conn.setAutoCommit(false);
            // 取得数据库的列名
            while (rs.next()) {
                TrusteeFeeInfo trusteeFeeInfo = new TrusteeFeeInfo();
                trusteeFeeInfo.setId((Long) rs.getObject(7));
                trusteeFeeInfo.setPymDt((String) rs.getObject(1));
                trusteeFeeInfo.setIntGrpCd((String) rs.getObject(2));
                trusteeFeeInfo.setPymAccNo((String) rs.getObject(3));
                trusteeFeeInfo.setIntGrpNm((String) rs.getObject(4));
                trusteeFeeInfo.setAmt((BigDecimal) rs.getObject(5));
                trusteeFeeInfo.setRcptAccNo((String) rs.getObject(6));
                trusteeFeeInfo.setIsRltv((String) rs.getObject(8));
                rsList.add(trusteeFeeInfo);
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

    /**
     * 关联数据的一系列操作 更新mysql中间表 删除hive表数据 插入hive表数据
     * @param trusteeFeeInfo
     * @throws Exception
     */
    private void insertIntoData(List<TrusteeFeeInfo> trusteeFeeInfo) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("ipport.properties");
        Connection connMysql = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        //更新中间表 关联状态
        updateMySqlTable(connMysql,trusteeFeeInfo);
        connMysql.close();
        //删除hive中 手工托管费关联表 //重新插入数据
        Connection connHive = DBUtils.getConnection(propertiesUtil.readProperty("hiveDriver"), propertiesUtil.readProperty("hiveUrl"), propertiesUtil.readProperty("hiveUsername"), propertiesUtil.readProperty("hivePassword"));
        deleteData4Hive(connHive);
        //重新插入数据
        TrusteeFeeInfo feeInfo = new TrusteeFeeInfo();
        feeInfo.setIsRltv("1");
        List<TrusteeFeeInfo> trusteeFeeInfos = queryTrusteeFeeTable(feeInfo, 1);
        insertDataToHive(connHive,trusteeFeeInfos);
        connHive.close();
    }


    /**
     * 更新mysql数据库 中间过渡表
     * @param conn
     * @param trusteeFeeInfo
     * @throws Exception
     */
    private void updateMySqlTable (Connection conn, List<TrusteeFeeInfo> trusteeFeeInfo) throws Exception{
        String sqlStr = "update M01_MNUL_RLTV_TSFE_DATA set Int_Grp_cd = ?,Int_Grp_Nm = ?,Is_Rltv='1',Src_Type='4' where id = ?";
        PreparedStatement stat = conn.prepareStatement(sqlStr);
        conn.setAutoCommit(false);
        if (trusteeFeeInfo != null && trusteeFeeInfo.size() > 0) {
            Iterator<TrusteeFeeInfo> iterator = trusteeFeeInfo.iterator();
            while (iterator.hasNext()) {
                TrusteeFeeInfo nexttrusteeFeeInfo = iterator.next();
                stat.setString(1, nexttrusteeFeeInfo.getIntGrpCd());
                stat.setString(2, nexttrusteeFeeInfo.getIntGrpNm());
                stat.setString(3, String.valueOf(nexttrusteeFeeInfo.getId()));
                stat.addBatch();
            }
        }
        try {
            stat.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }finally {
            stat.close();
        }
    }

    /**
     * 删除hive库中 托管关联信息表
     * @param conn
     */
    private void deleteData4Hive(Connection conn){
        try {
            conn.setAutoCommit(false);
            //删除hive表中数据
            Statement statement = conn.createStatement();
            statement.execute("truncate table mdata.M01_MNUL_RLTV_TSFE_DATA");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入mysql数据库 中间过渡表
     * @param conn
     * @param trusteeFeeInfo
     * @throws Exception
     */
    private void insertDataToHive (Connection conn, List<TrusteeFeeInfo> trusteeFeeInfo) throws Exception{
        conn.setAutoCommit(false);

        String sqlStr = "INSERT INTO TABLE mdata.M01_MNUL_RLTV_TSFE_DATA_TMP "
              //  + "PARTITION (yyyy=?,mm=?,dd=?)"
                + " VALUES (?,?,?,?,?,?,'4','1')";
        PreparedStatement stat = conn.prepareStatement(sqlStr);

        if (trusteeFeeInfo != null && trusteeFeeInfo.size() > 0) {
            Iterator<TrusteeFeeInfo> iterator = trusteeFeeInfo.iterator();
            while (iterator.hasNext()) {
                TrusteeFeeInfo nexttrusteeFeeInfo = iterator.next();
                stat.setString(1, nexttrusteeFeeInfo.getIntGrpCd());
                stat.setString(2, nexttrusteeFeeInfo.getIntGrpNm());
                stat.setString(2, nexttrusteeFeeInfo.getPymAccNo());
                stat.setString(2, nexttrusteeFeeInfo.getPymDt());
                stat.setString(2, String.valueOf(nexttrusteeFeeInfo.getAmt()));
                stat.setString(2, nexttrusteeFeeInfo.getRcptAccNo());
                stat.addBatch();
            }
        }
        try {
            stat.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }finally {
            stat.close();
        }
    }

    /**
     * 获取前一天的 年 月 日 等参数
     * @param CalendarPara
     * @return
     */
    private String getCalendarData(int CalendarPara) {
        String returnPara = "";
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        int para = 0;
        if (CalendarPara == Calendar.MONTH) {
            para = instance.get(CalendarPara) + 1;
        }else {
            para = instance.get(CalendarPara);
        }

        if (para < 10) {
            returnPara = "0" + para;
        } else {
            returnPara = String.valueOf(para);
        }
        return returnPara;
    }

}
