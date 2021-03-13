package com.alex.homework.week08sun02;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author user
 */
@SpringBootApplication
public class Week08Sun02Application implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Week08Sun02Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    private void clearData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from orders";
        statement.execute(sql);
        statement.close();
    }

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runnable fun = () -> {
            try {
                dbAction();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };

        new Thread(fun).start();
        new Thread(fun).start();
    }

    @ShardingTransactionType(TransactionType.XA)
    private void dbAction() throws SQLException {
        // sharding sphere 默认xa 是 Atomikos XA
        Connection connection = dataSource.getConnection();
        // 清除数据
        clearData(connection);
        connection.setAutoCommit(false);
        try {
            Statement statement = connection.createStatement();
            int i = 0;
            for (; i <= 15; i++) {
                String sql = "insert into orders(id,user_id,total_price,total_discount,status) values("+i+",i,1.00,0.00,'F')";
                statement.execute(sql);
            }
            connection.commit();
            System.out.println("==== Insert data");
            showData(connection);
        } catch (Exception e) {
            System.out.println("==== Rollback happens");
            connection.rollback();
            showData(connection);
        } finally {
            connection.commit();
            System.out.println("==== data after commit");
            showData(connection);
        }
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
}
