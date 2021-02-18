package com.alex.homework.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Alex Shen
 */
public class JdbcDemo {
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "sa");
        connectionProps.put("password", "1111");
        conn = DriverManager.getConnection("jdbc:h2:mem:jdbc-demo",connectionProps);

        Statement statement = conn.createStatement();

        System.out.println("===create table person");
        statement.execute("create table person(id varchar(36) primary key,name varchar(20))");

        System.out.println("===execute insert ===");
        UUID uuid = UUID.randomUUID();
        statement.execute("insert into person values('" + UUID.randomUUID()+ "','a')");
        statement.execute("insert into person values('" + UUID.randomUUID()+ "','b')");
        statement.execute("insert into person values('" + UUID.randomUUID()+ "','c')");
        statement.execute("insert into person values('" + uuid+ "','d')");

        System.out.println("===execute select ===");
        String query = "select * from person where 1=1";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            System.out.println("name:" + rs.getString("name"));
        }

        System.out.println("===execute update ===");
        statement.executeUpdate("update person set name='e' where id='" + uuid + "'");
        System.out.println("===after update ===");
        rs = statement.executeQuery(query);
        while (rs.next()) {
            System.out.println("name:" + rs.getString("name"));
        }

        System.out.println("===execute delete ===");
        statement.executeUpdate("delete from person where id='" + uuid + "'");
        System.out.println("===after delete ===");
        rs = statement.executeQuery(query);
        while (rs.next()) {
            System.out.println("name:" + rs.getString("name"));
        }
        statement.close();
    }
}
