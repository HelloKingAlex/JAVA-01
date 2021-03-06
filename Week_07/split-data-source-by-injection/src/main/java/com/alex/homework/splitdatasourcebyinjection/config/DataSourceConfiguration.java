package com.alex.homework.splitdatasourcebyinjection.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author Alex Shen
 */
@Configuration
public class DataSourceConfiguration {
    @Bean(name="dataSource01")
    public DataSource getDataSource01() {
        final String USER = "root";
        final String PASSWORD  = "123456";
        final String URL = "jdbc:mysql://localhost:33061/geektime_e_shop";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        return new HikariDataSource(config);
    }

    @Bean(name="dataSource02")
    public DataSource getDataSource02() {
        final String USER = "root";
        final String PASSWORD  = "123456";
        final String URL = "jdbc:mysql://localhost:33062/geektime_e_shop";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        return new HikariDataSource(config);
    }
}
