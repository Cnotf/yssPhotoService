package com.yss.cnotf.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @Author: cnotf
 * @Description: 利用反射机制从ResultSet自动绑定到JavaBean；根据记录集自动调用javaBean里边的对应方法
 * @Date: Create in 14:33 2019/07/09
 */
public class ResultToBeanUtil<T> {

    /**
     * @param clazz
     *            所要封装的javaBean
     * @param rs
     *            记录集
     * @return ArrayList 数组里边装有 多个javaBean
     * @throws Exception
     */
    public List<T> getList(Class<T> clazz, ResultSet rs) {
        Field field = null;
        List<T> lists = new ArrayList<T>();
        try {
            // 获取列名
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取记录集中的列数
            int counts = rsmd.getColumnCount();
            // 定义counts个String 变量
            String[] columnNames = new String[counts];
            for (int i = 0; i < counts; i++) {

                columnNames[i] = rsmd.getColumnLabel(i + 1);
            }
            //把bean中的field域名称 存 在数组中，用于后面比较
            Field[] declaredFields = clazz.getDeclaredFields();
            String[] beanFieldNames = new String[declaredFields.length];
            for (int i = 0,length = declaredFields.length; i < length; i++){
                beanFieldNames[i] = declaredFields[i].getName().toUpperCase();
            }
            // 变量ResultSet
            while (rs.next()) {
                T t = clazz.newInstance();
                // 反射, 从ResultSet绑定到JavaBean
                for (int i = 0; i < counts; i++) {
                    //如果名称中包含下划线，则去掉下划线
                    // 查询bean中是否有数据库查询的列（字母全部转换成大写比较）
                    int index = Arrays.asList(beanFieldNames).indexOf(columnNames[i].replaceAll("_","").toUpperCase());
                    if (index > -1) {
                        field = clazz.getDeclaredField(declaredFields[index].getName());
                        // 这里是获取bean属性的类型
                        Class<?> beanType = field.getType();
                        // 根据 rs 列名 ，组装javaBean里边的其中一个set方法，object 就是数据库第一行第一列的数据了
                        Object value = rs.getObject(columnNames[i]);
                        if (value != null) {
                            // 这里是获取数据库字段的类型
                            Class<?> dbType = value.getClass();
                            // 处理日期类型不匹配问题
                            if (dbType == java.sql.Timestamp.class
                                    && beanType == java.util.Date.class) {
                                value = new java.util.Date(
                                        ((java.sql.Timestamp) value).getTime());
                            }
                            // 处理double类型不匹配问题
                            if (dbType == java.math.BigDecimal.class
                                    && beanType == double.class) {
                                value = new Double(value.toString());
                            }
                            // 处理int类型不匹配问题
                            if (dbType == java.math.BigDecimal.class
                                    && beanType == int.class) {
                                value = new Integer(value.toString());
                            }
                        }
                        //首字母大写 并用set拼接
                        String setMethodName = "set"
                                + StringUtils.captureName(field.getName());
                        // 第一个参数是传进去的方法名称，第二个参数是 传进去的类型；
                        Method m = t.getClass().getMethod(setMethodName, beanType);
                        // 第二个参数是传给set方法数据；如果是get方法可以不写
                        m.invoke(t, value);
                    } else {
                        continue;
                    }
                }
                lists.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lists;
    }

}
