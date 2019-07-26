package com.yss.cnotf.services;

import com.yss.cnotf.util.PropertiesUtil;
import com.yss.cnotf.util.ResultToBeanUtil;

import javax.jdo.annotations.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cnotf
 * @Description:报表拍照处理流程
 * @Date: Create in 17:00 2019/05/24
 */
public class BiDateService {

    public static void biService (String paraAdd, String tablename, List<BiDateInfo> biDateInfos) throws Exception{
        BiDateService biDateService = new BiDateService();
        biDateService.insertIntoDataofMysql(paraAdd, tablename, biDateInfos);
    }

    @Transactional
    private void insertIntoDataofMysql(String paraAdd, String tablename, List<BiDateInfo> biDateInfos) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        deleteBiData(conn,paraAdd,tablename);
        insertIntoBiTable(conn,paraAdd,tablename);
        saveBiDataList(biDateInfos);
        conn.close();
    }


    /**
     * 保存报表数据到mysql  以供查询下载
     * @param biDateInfos
     * @throws Exception
     */
    public void saveBiDataList(List<BiDateInfo> biDateInfos) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        if (biDateInfos == null || biDateInfos.size() == 0) {
            throw new Exception();
        }
        String insertSql = "insert into etl_downloadexcel_data(bi_name,start_photo_date,end_photo_date,create_date,operate) values ";
        StringBuilder sb = new StringBuilder();
        try (Connection conn =
                     DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
             Statement stat =  conn.createStatement()) {
            conn.setAutoCommit(false);
            for (BiDateInfo biDateInfo : biDateInfos) {
                sb.append("('" + biDateInfo.getBiName() + "','" +
                        biDateInfo.getStartPhotoDate() + "','" + biDateInfo.getEndPhotoDate() + "',NOW(),null),");
                insertSql = insertSql + sb.toString();
            }
            stat.execute(insertSql.substring(0,insertSql.length()-1));
            conn.commit();
        }
    }

    /**
     * 查询报表数据
     * @param biDateInfo
     * @return
     * @throws Exception
     */
    public List<BiDateInfo> queryBiDataList (BiDateInfo biDateInfo) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        List<BiDateInfo> rsList = new ArrayList<BiDateInfo>();
        String queryStr = "SELECT id,bi_name,start_photo_date,end_photo_date,create_date,operate FROM etl_downloadexcel_data where 1=1 ";
        StringBuilder stringBuilder = appendBiQueryCriteria(biDateInfo, queryStr);
        String pageParamStr = "";
        if (biDateInfo.getPage() != null && biDateInfo.getRows() != null) {
            pageParamStr = " limit " + (biDateInfo.getPage()-1)*biDateInfo.getRows()+","+biDateInfo.getRows();
        }

        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(stringBuilder.toString() + pageParamStr)){
            //通过反射给bean赋值
            ResultToBeanUtil<BiDateInfo> biDateInfoResultToBeanUtil = new ResultToBeanUtil<BiDateInfo>();
            rsList = biDateInfoResultToBeanUtil.getList(BiDateInfo.class, rs);
            if (rsList != null && rsList.size() > 0) {
                rsList.get(0).setTotal(queryBiCount(conn, biDateInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return rsList;
    }

    /**
     * 查询 报表的总条数
     * @param biDateInfo
     * @return
     * @throws Exception
     */
    public Integer queryBiCount(Connection conn,BiDateInfo biDateInfo) throws Exception{
        Integer count = 0;
        String countStr = "SELECT count(*) total FROM etl_downloadexcel_data where 1=1 ";
        StringBuilder stringBuilder = appendBiQueryCriteria(biDateInfo, countStr);
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
     * @param biDateInfo
     * @param queryStr
     * @return
     */
    private StringBuilder appendBiQueryCriteria(BiDateInfo biDateInfo,String queryStr) {
        StringBuilder stringBuilder = new StringBuilder(queryStr);
        if (biDateInfo.getBiName() != null && !"".equals(biDateInfo.getBiName())) {
            stringBuilder.append("and bi_name like '%" + biDateInfo.getBiName()).append("%'");
        }
        if (biDateInfo.getStartPhotoDate() != null && !"".equals(biDateInfo.getStartPhotoDate())) {
            stringBuilder.append("and start_photo_date = '" + biDateInfo.getStartPhotoDate()).append("'");
        }
        if (biDateInfo.getEndPhotoDate() != null && !"".equals(biDateInfo.getEndPhotoDate())) {
            stringBuilder.append("and end_photo_date = '" + biDateInfo.getEndPhotoDate()).append("'");
        }
        return stringBuilder;
    }

    /**
     * 删除手工拍照重复数据
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void deleteBiData(Connection conn, String pataAdd, String tablename) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("job.properties");
        try (Statement stat =
                     conn.createStatement()) {
            stat.addBatch("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE1= '" + pataAdd + "' AND DATA_DT='" + DateUtils.getDateToString() + "' AND SOURCE_FLAG='A' AND COLUMN1 = '"+ propertiesUtil.readProperty("statinfo") +"'");
            stat.addBatch("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE2= '" + pataAdd + "' AND DATA_DT='" + DateUtils.getDateToString() + "' AND SOURCE_FLAG='B' and COLUMN1 = '" + tablename + "'");
            stat.executeBatch();
        }
    }

    /**
     * 插入数据库
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void insertIntoBiTable (Connection conn, String pataAdd, String tablename) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("job.properties");
        try (Statement stat =
                     conn.createStatement()){
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+DateUtils.getDateToString()+"','"+pataAdd+ "|" +tablename+"','','A','0','"+propertiesUtil.readProperty("statinfo")+"','');");
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+ DateUtils.getDateToString() + "','','" + pataAdd + "','B','0','" + tablename + "','')");
            stat.executeBatch();
        }
    }
}
