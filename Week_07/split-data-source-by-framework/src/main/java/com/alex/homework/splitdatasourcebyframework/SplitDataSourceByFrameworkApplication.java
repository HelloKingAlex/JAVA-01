package com.alex.homework.splitdatasourcebyframework;

import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Shen
 */
@SpringBootApplication
public class SplitDataSourceByFrameworkApplication implements ApplicationRunner {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SplitDataSourceByFrameworkApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Connection connection1 = dataSource.getConnection();
        final long COUNTS = 100;

        for (int i = 1; i <= COUNTS; i++) {
            Statement statement = connection1.createStatement();
            String sql = "insert into `geektime_e_shop`.`order`(id,user_id,total_price,total_discount,status) values("+ i +"," + i+ ",1.00,0.00,'F')";
            statement.execute(sql);
        }
        Connection connection2 = dataSource.getConnection();

        for (int i = 1; i <= COUNTS; i++) {
            Statement statement = connection2.createStatement();
            String sql = "insert into `geektime_e_shop`.`order`(id,user_id,total_price,total_discount,status) values("+ i +"," + i+ ",1.00,0.00,'F')";
            statement.execute(sql);
        }
        connection1.close();
        connection2.close();
    }
}
