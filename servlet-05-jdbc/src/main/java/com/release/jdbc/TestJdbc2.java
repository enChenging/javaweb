package com.release.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author yancheng
 * @since 2022/7/6
 */
public class TestJdbc2 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //配置信息
        String url = "jdbc:mysql://localhost:3306/DB01?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&rewriteBatchedStatements=true&autoReconnect=true";
        String username = "root";
        String password = "123456";

        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        //3.编写SQL
        String sql = "insert into dept (deptno, dname, db_source) values (?,?,?)";
        //4.预编译
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 6);
        preparedStatement.setString(2, "运营部");
        preparedStatement.setString(3, "db02");
        //5.执行SQL
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            System.out.println("插入成功");
        }
        //6.关闭连接，释放资源，先开后关
        preparedStatement.close();
        connection.close();
    }
}
