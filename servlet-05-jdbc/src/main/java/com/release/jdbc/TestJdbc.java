package com.release.jdbc;

import java.sql.*;

/**
 * @author yancheng
 * @since 2022/7/6
 */
public class TestJdbc {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //配置信息
        String url = "jdbc:mysql://localhost:3306/DB01?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true&autoReconnect=true";
        String username = "root";
        String password = "123456";

        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        //3.向数据库发送SQL的对象Statement:CRUD
        Statement statement = connection.createStatement();
        //4.编写SQL
        String sql  = "select * from dept";
        //5.执行查询SQL，返回一个ResultSet ： 结果集
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            System.out.println("deptno = "+rs.getObject("deptno"));
            System.out.println("dname = "+rs.getObject("dname"));
            System.out.println("db_source = "+rs.getObject("db_source"));
        }

        //6.关闭连接，释放资源，先开后关
        rs.close();
        statement.close();
        connection.close();
    }
}
