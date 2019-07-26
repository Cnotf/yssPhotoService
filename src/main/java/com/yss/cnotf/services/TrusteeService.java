package com.yss.cnotf.services;

import com.yss.cnotf.util.PropertiesUtil;
import com.yss.cnotf.util.ResultToBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 13:43 2019/06/17
 */
public class TrusteeService {


    private final static Logger logger = LoggerFactory.getLogger(TrusteeService.class);

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
        return trusteeService.queryTrusteeFeeTable(trusteeFeeInfo);
    }


    /**
     * 查询mysql中托管数据
     * @param trusteeFee
     * @return
     * @throws Exception
     */
    private List<TrusteeFeeInfo> queryTrusteeFeeTable (TrusteeFeeInfo trusteeFee) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        List<TrusteeFeeInfo> rsList = new ArrayList<TrusteeFeeInfo>();
        String queryStr = "select Pym_Dt,Int_Grp_cd,Pym_Acc_No,Int_Grp_Nm,Amt,Rcpt_Acc_No,id,Is_Rltv from  m01_mnul_rltv_tsfe_data where 1=1 ";
        StringBuilder stringBuilder = appendQueryCriteria(trusteeFee, queryStr);
        String limitStr = " limit " + (trusteeFee.getPage()-1)*trusteeFee.getRows()+","+trusteeFee.getRows();
        try (PreparedStatement stat = conn.prepareStatement(stringBuilder.toString() + limitStr);
             ResultSet rs = stat.executeQuery()){
            //通过反射给bean赋值
            ResultToBeanUtil<TrusteeFeeInfo> handDateInfoResultToBeanUtil = new ResultToBeanUtil<TrusteeFeeInfo>();
            rsList = handDateInfoResultToBeanUtil.getList(TrusteeFeeInfo.class, rs);
            if (rsList != null && rsList.size() > 0) {
                rsList.get(0).setTotal(queryTrusteeCount(conn, trusteeFee));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();

        }
        return rsList;
    }

    /**
     * 查询mysql中维护的托管费数据 的总条数
     * @param trusteeFee
     * @return
     * @throws Exception
     */
    public Integer queryTrusteeCount(Connection conn,TrusteeFeeInfo trusteeFee) throws Exception{
        Integer count = 0;
        String countStr = "select count(*) total from  m01_mnul_rltv_tsfe_data where 1=1 ";
        StringBuilder stringBuilder = appendQueryCriteria(trusteeFee, countStr);
        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(stringBuilder.toString())){
            if (rs.first()){
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 拼接查询条件
     * @param trusteeFee
     * @param queryStr
     * @return
     */
    private StringBuilder appendQueryCriteria(TrusteeFeeInfo trusteeFee,String queryStr) {
        StringBuilder stringBuilder = new StringBuilder(queryStr);
        if (trusteeFee.getIsRltv() != null && !"".equals(trusteeFee.getIsRltv())) {
            stringBuilder.append("and Is_Rltv  = '" + trusteeFee.getIsRltv()).append("'");
        }
        if (trusteeFee.getIntGrpCd() != null && !"".equals(trusteeFee.getIntGrpCd())) {
            stringBuilder.append("and Int_Grp_cd = '" + trusteeFee.getIntGrpCd()).append("'");
        }
        if (trusteeFee.getPymDt() != null && !"".equals(trusteeFee.getPymDt())) {
            stringBuilder.append("and Pym_Dt = '" + trusteeFee.getPymDt()).append("'");
        }
        if (trusteeFee.getIntGrpNm() != null && !"".equals(trusteeFee.getIntGrpNm())) {
            stringBuilder.append("and Int_Grp_Nm = '" + trusteeFee.getIntGrpNm()).append("'");
        }
        if (trusteeFee.getPymAccNo() != null && !"".equals(trusteeFee.getPymAccNo())) {
            stringBuilder.append("and Pym_Acc_No = '" + trusteeFee.getPymAccNo()).append("'");
        }
        if (trusteeFee.getRcptAccNo() != null && !"".equals(trusteeFee.getRcptAccNo())) {
            stringBuilder.append("and Rcpt_Acc_No = '" + trusteeFee.getRcptAccNo()).append("'");
        }
        if (trusteeFee.getId() != null && trusteeFee.getId() != 0L) {
            stringBuilder.append("and id = '" + trusteeFee.getId()).append("'");
        }
        return stringBuilder;
    }

    /**
     * 关联数据的一系列操作 更新mysql中间表 删除hive表数据 插入hive表数据
     * @param trusteeFeeInfo
     * @throws Exception
     */
    private void insertIntoData(List<TrusteeFeeInfo> trusteeFeeInfo) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection connMysql = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        //更新中间表 关联状态
        updateMySqlTable(connMysql,trusteeFeeInfo);
        //对hive数据库操作由jdbc改为sh脚本操作
        executeSH();
        //更新状态通知大数据监控
        updateFlag(connMysql);
        connMysql.close();
    }

    private List<String> executeSH()  throws Exception {
        List<String> stringList = new ArrayList<>();
        PropertiesUtil propertiesUtil = new PropertiesUtil("shellpath.properties");
        //可以执行脚本
        String command = propertiesUtil.readProperty("shellpath");
        logger.info("脚本路径：" + command);
        //可以执行带参数的脚本
//        String[] command = {"/usr/local/RPFiles/transStr.sh", "test"};
        Process ps = Runtime.getRuntime().exec(command);
        int exitValue = ps.waitFor();
        //当返回值为0时表示执行成功
        if (0 != exitValue){
            logger.info("call shell failed. error code is :" + exitValue);
        }
        //只能接收脚本echo打印的数据，并且是echo打印的最后一次数据，如果想打印所有数据，可以参考本篇文章的脚本编写
        BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            logger.info("脚本返回的数据如下： " + line);
            stringList.add(line);
        }
        in.close();
        br.close();
        return stringList;

    }


    /**
     * 更新通知状态
     * @param conn
     * @throws Exception
     */
    private void updateFlag(Connection conn)  throws Exception{
        String sqlStr = "update ETL_HIVEDATA_FLAG set FLAG = '1'";
        Statement stat = conn.createStatement();
        conn.setAutoCommit(false);
        try {
            stat.executeUpdate(sqlStr);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }finally {
            stat.close();
        }
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
            statement.execute("truncate table mdata.M01_MNUL_RLTV_TSFE_DATA_TMP");
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
