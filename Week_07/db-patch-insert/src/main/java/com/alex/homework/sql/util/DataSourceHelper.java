package com.alex.homework.sql.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库操作帮助类
 * 用于数据库基本信息的配置
 *
 * @author Alex Shen
 */
public class DataSourceHelper {
    private final String USER = "root";
    private final String PASSWORD  = "123456";
    private final String URL = "jdbc:mysql://localhost:33061/geektime_e_shop";
    private HikariDataSource ds;
    public DataSourceHelper() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setConnectionTimeout(30000_000);
        this.ds = new HikariDataSource(config);
    }
    public HikariDataSource getDataSource() {
        return this.ds;
    }

    public void close() {
        ds.close();
    }
}
