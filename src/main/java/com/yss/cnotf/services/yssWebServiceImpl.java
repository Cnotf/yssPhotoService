package com.yss.cnotf.services;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: cnotf
 * @Description:
 * @Date: Create in 11:00 2019/04/30
 */
@WebService(
        endpointInterface = "com.yss.cnotf.services.yssWebServiceI",
        portName = "yssWebServiceWSPort",
        serviceName = "yssWebServiceWSService",
        targetNamespace = "http://com.yss.hello.service")
public class yssWebServiceImpl implements yssWebServiceI {

    /**
     * 接受报表拍照参数，并处理
     * @param startPhotoDate
     * @param endPhotoDate
     * @param startAccountDate
     * @param endAccountDate
     * @return
     */
    @Override
    public String getBiDate(String startPhotoDate, String endPhotoDate, String startAccountDate, String endAccountDate) {
        //添加服务端处理流程
        String dataadd=startPhotoDate+"|"+endPhotoDate+"|"+startAccountDate+"|"+startAccountDate;
        String returnFlag = "1";
        try {
            insertintoDataofMysql(dataadd);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }

        return returnFlag;
    }

    /**
     * 接受手工拍照参数，并处理
     * @param beginHandDate
     * @param endHandDate
     * @return
     */
    @Override
    public String getHandDate(String beginHandDate, String endHandDate) {
        //添加服务端处理流程
        String dataadd=beginHandDate+"|"+endHandDate;
        String returnFlag = "1";
        try {
            insertintoDataofMysql(dataadd);
        } catch (Exception e) {
            returnFlag = "2";
            e.printStackTrace();
        }
        return returnFlag;
    }

    public void insertintoDataofMysql(String dataadd) throws Exception {
        Connection conn = DBUtils.newConnection("MYSQL","10.7.53.28","3306","mpetl","mpetl","mpetl");
        DelData(conn,dataadd);

    }

    private void DelData(Connection conn,String dataadd) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String utilDate = sdf.format(date).toString();
        int delflag = dataadd.split("|").length;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        if (delflag == 2){
            stat = conn.prepareStatement("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE1= '" + dataadd + "' AND DATA_DT='" + utilDate + "' AND SOURCE_FLAG='A';");
            try {
                stat.execute();
            }  catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stat != null) {
                    stat.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            stat1 = conn.prepareStatement("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+utilDate+"','"+dataadd+"','','A','0','','')");
            try {
                stat1.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                stat1.close();
            }
            }
         else{
            try {
                stat = conn.prepareStatement("DELETE FROM  ETL_WEBSERVICE_DATA WHERE DATA_SOURCE2= '"+dataadd+"' AND DATA_DT='"+utilDate+"' AND SOURCE_FLAG='B';");
                stat.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stat != null) {
                    stat.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            try {
                stat1 = conn.prepareStatement("INSERT INTO ETL_WEBSERVICE_DATA VALUES ('"+utilDate+"','','"+dataadd+"','B','0','','')");
                stat1.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stat1 != null) {
                    stat1.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        }




    }
}


