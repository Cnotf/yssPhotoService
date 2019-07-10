package com.yss.cnotf.services;

import com.yss.cnotf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @Author: cnotf
 * @Description: 手工拍照数据处理
 * @Date: Create in 16:42 2019/05/24
 */
public class HandDateService {

    private final static Logger logger = LoggerFactory.getLogger(HandDateService.class);

    public static void handService (List<HandDateInfo> handDateInfoList, String photoType) throws Exception{
        HandDateService handDateService = new HandDateService();
        //手工拍照
        if ("0".equals(photoType)) {
            handDateService.insertIntoDataofMysqlHand(handDateInfoList, photoType);
        } else {
            //自动拍照
            handDateService.insertIntoDataofMysqlAuto(photoType);
        }
    }

    /**
     * 自动拍照
     * @param photoType
     * @throws Exception
     */
    private void insertIntoDataofMysqlAuto(String photoType) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        try (Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"))){
            HandDateInfo handDateInfo = new HandDateInfo();
            //获取昨天日期字符串 由于定时任务为凌晨跑，需要获取前一天日期
            String yesterdayDateStr = CommonUtils.dateToString("yyyy-MM-dd", CommonUtils.getAnyDayDate(new Date(), Calendar.DAY_OF_MONTH, -1));
            handDateInfo.setPhotoOperationDate(yesterdayDateStr);
            handDateInfo.setPhotoStatus("0");
            List<HandDateInfo> handDateInfos = queryPhotoDataList(handDateInfo);
            for (HandDateInfo handDateInfo1 : handDateInfos) {
                String paraAdd = handDateInfo1.getPhotoOperationDate()+"|"+handDateInfo1.getPhotoDate();
                logger.info("自动拍照参数=============："+paraAdd);
                System.out.println("自动拍照参数=============："+paraAdd);
                deleteHandData(conn,paraAdd);
                insertIntoHandTable(conn,paraAdd);
                updatePhotoDataStatus(conn,handDateInfo1,photoType);
            }
        }
    }

    /**
     * 手动拍照
     * @param handDateInfoList
     * @param photoType
     * @throws Exception
     */
    private void insertIntoDataofMysqlHand(List<HandDateInfo> handDateInfoList, String photoType) throws Exception {
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        try (Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"))){
            if (handDateInfoList == null || handDateInfoList.size() == 0) {
                throw new Exception();
            }
            for (HandDateInfo handDateInfo : handDateInfoList) {
                String paraAdd = handDateInfo.getPhotoOperationDate()+"|"+handDateInfo.getPhotoDate();
                logger.info("手动拍照参数=============："+paraAdd);
                System.out.println("手动拍照参数=============："+paraAdd);
                deleteHandData(conn,paraAdd);
                insertIntoHandTable(conn,paraAdd);
                updatePhotoDataStatus(conn,handDateInfo,photoType);
            }
        }
    }


    /**
     * 删除手工拍照重复数据
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void deleteHandData(Connection conn, String pataAdd) throws Exception{
        try (PreparedStatement stat =
                     conn.prepareStatement("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE1= '" + pataAdd + "' AND DATA_DT='" + DateUtils.getDateToString() + "' AND SOURCE_FLAG='A'")){
            stat.execute();
        }
    }

    /**
     * 插入数据库
     * @param conn
     * @param pataAdd
     * @throws Exception
     */
    private void insertIntoHandTable (Connection conn, String pataAdd) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("job.properties");
        try (Statement stat =  conn.createStatement()) {
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+DateUtils.getDateToString()+"','"+pataAdd+"','','A','0','"+propertiesUtil.readProperty("grpinfo")+"','');");
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+DateUtils.getDateToString()+"','"+pataAdd+"','','A','0','"+propertiesUtil.readProperty("statinfo")+"','');");
            stat.addBatch("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+DateUtils.getDateToString()+"','"+pataAdd+"','','A','0','"+propertiesUtil.readProperty("datainfo")+"','');");
            stat.executeBatch();
        }
    }


    /**
     * 查询mysql中维护的拍照数据
     * @param handDateInfo
     * @return
     * @throws Exception
     */
    public List<HandDateInfo> queryPhotoDataList (HandDateInfo handDateInfo) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        Connection conn = DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
        List<HandDateInfo> rsList = new ArrayList<HandDateInfo>();
        String queryStr = "SELECT Id id,PhotoOperationDate photo_Operation_Date,PhotoDate,CreateDate,PhotoStatus,PhotoType FROM etl_photo where DeleteFlag = '0' ";
        StringBuilder stringBuilder = new StringBuilder(queryStr);
        if (handDateInfo.getPhotoOperationDate() != null && !"".equals(handDateInfo.getPhotoOperationDate())) {
            stringBuilder.append("and PhotoOperationDate = '" + handDateInfo.getPhotoOperationDate()).append("'");
        }
        if (handDateInfo.getPhotoDate() != null && !"".equals(handDateInfo.getPhotoDate())) {
            stringBuilder.append("and PhotoDate = '" + handDateInfo.getPhotoDate()).append("'");
        }
        if (handDateInfo.getPhotoStatus() != null && !"".equals(handDateInfo.getPhotoStatus())) {
            stringBuilder.append("and PhotoStatus = '" + handDateInfo.getPhotoStatus()).append("'");
        }
        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(stringBuilder.toString())){
            //通过反射给bean赋值
            ResultToBeanUtil<HandDateInfo> handDateInfoResultToBeanUtil = new ResultToBeanUtil<HandDateInfo>();
            rsList = handDateInfoResultToBeanUtil.getList(HandDateInfo.class, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return rsList;
    }

    /**
     * 删除拍照信息
     * @param handDateInfoList
     * @return
     */
    public Integer deleteHandPhotoData (List<HandDateInfo> handDateInfoList) throws Exception{
        Integer returnVal = 2;
        if (handDateInfoList == null || handDateInfoList.size() == 0) {
            throw new Exception();
        }
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        StringBuilder sb = new StringBuilder();
        String preSql = "update etl_photo set DeleteFlag ='1' WHERE Id in (";
        for (HandDateInfo handDateInfo : handDateInfoList) {
            if (handDateInfo.getId() != null) {
                sb.append(handDateInfo.getId()).append(",");
            } else {
                sb.append(0).append(",");
            }
        }
        String sql = preSql + sb.substring(0, sb.length()-1) + ")";
        try (Connection conn =
                     DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
             PreparedStatement stat =
                     conn.prepareStatement(sql)){
            int executeUpdateCount = stat.executeUpdate();
            if (executeUpdateCount > 0) {
                //返回更新成功的标志
                returnVal = 1;
            }
        }
        return returnVal;

    }

    /**
     * 保存拍照信息
     * @param handDateInfoList
     * @throws Exception
     */
    public void savePhotoDataList (List<HandDateInfo> handDateInfoList) throws Exception{
        PropertiesUtil propertiesUtil = new PropertiesUtil("databaseconfig.properties");
        if (handDateInfoList == null || handDateInfoList.size() == 0) {
            throw new Exception();
        }
        String insertSql = "insert into etl_photo(PhotoOperationDate,PhotoDate,CreateDate,PhotoStatus,PhotoType,DeleteFlag) values ";
        StringBuilder sb = new StringBuilder();
        try (Connection conn =
                     DBUtils.getConnection(propertiesUtil.readProperty("mysqlDriver"), propertiesUtil.readProperty("mysqlUrl"), propertiesUtil.readProperty("mysqlUsername"), propertiesUtil.readProperty("mysqlPassword"));
             Statement stat =  conn.createStatement()) {
            conn.setAutoCommit(false);
            String sql = "";
            for (HandDateInfo handDateInfo : handDateInfoList) {
                //新增 由于前台页面选择的信息为 自动拍照 所以这边 拍照类型直接赋值 1
                if (handDateInfo.getId() == null ) {
                    sb.append("('"+ handDateInfo.getPhotoOperationDate() + "','" +
                            handDateInfo.getPhotoDate() + "',NOW(),'0','1','0')");
                    sql = insertSql + sb.toString();
                } else {
                    //更新
                    sql = "update etl_photo set PhotoOperationDate = '"+handDateInfo.getPhotoOperationDate()
                            +"',PhotoDate = '"+ handDateInfo.getPhotoDate() +"' where id = " + handDateInfo.getId();
                }
                stat.execute(sql);
            }
            conn.commit();
        }
    }

    /**
     * 更新拍照状态和拍照类型
     * @param conn
     * @param handDateInfo
     * @param photoType
     * @throws Exception
     */
    private void updatePhotoDataStatus (Connection conn, HandDateInfo handDateInfo, String photoType) throws Exception{
        String sqlStr = "update etl_photo set PhotoStatus = '1' where id = " + handDateInfo.getId();
        //如果手动拍照 直接插入一条 已拍照状态 手工拍照状态的数据
        if ("0".equals(photoType)) {
            sqlStr = "insert into etl_photo(PhotoOperationDate,PhotoDate,CreateDate,PhotoStatus,PhotoType,DeleteFlag) values ('"+ handDateInfo.getPhotoOperationDate() + "','"+
                    handDateInfo.getPhotoDate() + "',NOW(),'1','"+ photoType +"','0')";
        }
        try (Statement stat = conn.createStatement()){
            stat.execute(sqlStr);

        }
    }
}
