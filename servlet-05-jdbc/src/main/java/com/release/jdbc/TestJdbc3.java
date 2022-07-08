package com.release.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author yancheng
 * @since 2022/7/6
 */
public class TestJdbc3 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //配置信息
        String url = "jdbc:mysql://localhost:3306/DB01?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true&autoReconnect=true";
        String username = "root";
        String password = "123456";

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            //通知数据库开启事务，false 开启
            connection.setAutoCommit(false);
            String sql = "update dept set db_source = 'db03' where deptno = 1";
            connection.prepareStatement(sql).executeUpdate();

//            int i = 1 / 0;

            String sql2 = "update dept set db_source = 'db03' where deptno = 2";
            connection.prepareStatement(sql2).executeUpdate();
            connection.commit();
            System.out.println("提交成功");
        } catch (Exception e) {
            try {
                //如果出现异常，就通知数据库回滚事务
                connection.rollback();
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
