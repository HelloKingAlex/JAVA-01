package com.alex.homework.week08wed02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Alex Shen
 */
@SpringBootApplication
public class Week08Wed02Application implements ApplicationRunner {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Week08Wed02Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Connection connection = dataSource.getConnection();
        // 清除数据
        clearData(connection);
        // 写入数据
        insertData(connection);

        // 显示数据
        showData(connection);
        connection.close();
    }

    private void showData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select id, user_id from orders";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            System.out.println("id:" + rs.getString("id") + ", user_id:" + rs.getString("user_id"));
        }
        statement.close();
    }
    private void insertData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        // user id = 1,order id = 1
        String sql = "insert into orders(id,user_id,total_price,total_discount,status) values(1,1,100,0,'F')";
        statement.execute(sql);
        // user id = 1,order id = 2
        sql = "insert into orders(id,user_id,total_price,total_discount,status) values(2,1,100,0,'F')";
        statement.execute(sql);
        // user id = 2,order id = 3
        sql = "insert into orders(id,user_id,total_price,total_discount,status) values(3,2,100,0,'F')";
        statement.execute(sql);
        // user id = 2,order id = 4
        sql = "insert into orders(id,user_id,total_price,total_discount,status) values(4,2,100,0,'F')";
        statement.execute(sql);
        // user id = 3, order id = 16-31

        for (int i = 16; i <= 31; i++) {
            sql = "insert into orders(id,user_id,total_price,total_discount,status) values("+i+",3,1.00,0.00,'F')";
            statement.execute(sql);
        }
        statement.close();
    }
    private void clearData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from orders";
        statement.execute(sql);
        statement.close();
    }
}
