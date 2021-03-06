package com.alex.homework.splitdatasourcebyframework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @author Alex Shen
 */
@SpringBootApplication
public class SplitDataSourceByFrameworkApplication implements ApplicationRunner {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(SplitDataSourceByFrameworkApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Connection connection = dataSource.getConnection();
        final long COUNTS = 100;

        for (int i = 1; i <= COUNTS; i++) {
            Statement statement = connection.createStatement();
            String sql = "insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values("+ i +"," + i+ ",1.00,0.00,'F')";
            statement.execute(sql);
        }
        connection.close();
    }
}
