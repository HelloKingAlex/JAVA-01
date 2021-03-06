package com.alex.homework.splitdatasourcebyinjection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLException;


/**
 * @author Alex Shen
 */

@SpringBootApplication()
public class SplitDataSourceByInjectionApplication implements ApplicationRunner{

    @Autowired
    @Qualifier("dataSource01")
    DataSource dataSource01;
    @Autowired
    @Qualifier("dataSource02")
    DataSource dataSource02;
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(SplitDataSourceByInjectionApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sql = "insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values(1,1,1.00,0.00,'F')";
        dataSource01.getConnection().createStatement().execute(sql);
        dataSource02.getConnection().createStatement().execute(sql);
    }
}
