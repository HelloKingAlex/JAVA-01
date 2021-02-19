package com.alex.homework.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Alex Shen
 */
public class HikariConnectionPoolDemo {
    private static Logger logger = LoggerFactory.getLogger(HikariConnectionPoolDemo.class);
    public static void main(String[] args) throws SQLException, InterruptedException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:~/h2/hikaricp-demo");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize","20");
        config.addDataSourceProperty("preStmtCacheSqlLimit","2048");

        // 默认值应该是cpu核数（待确定，猜测应该是这样）
        logger.info("===pool default size:" + String.valueOf(config.getMaximumPoolSize()));
        config.setMaximumPoolSize(4);
        logger.info("===pool max size:" + String.valueOf(config.getMaximumPoolSize()));
        // 如果连接池设置的时间太短那么，hikari cp 将提示
        // maxLifetime is less than 30000ms, setting to default 1800000ms.
        // 这个怪异的数字只是为了测试
        config.setMaxLifetime(30001);

        HikariDataSource ds = new HikariDataSource(config);
        ds.getConnection()
                .createStatement()
                .execute("drop table person");
        ds.getConnection()
                .createStatement()
                .execute("create table person(id integer primary key,name varchar(20))");

        for (int i = 0; i < 1000; i++) {
            System.out.println("connections:"+i);
            //每次循环都获取一个连接，用于给连接池施加压力
            Connection connection = ds.getConnection();
            connection.createStatement().execute("insert into person values(" + i + ",'name"+i+"')");
            connection.close();
        }
        ds.close();
        Thread.sleep(1000);

        ds = new HikariDataSource(config);
        ds.getConnection()
                .createStatement()
                .execute("drop table person");
        ds.getConnection()
                .createStatement()
                .execute("create table person(id integer primary key,name varchar(20))");
        for (int i = 0; i < 1000; i++) {
            System.out.println("connections:"+i);
            //每次循环都获取一个连接，用于给连接池施加压力
            Connection connection = ds.getConnection();
            connection.createStatement().execute("insert into person values(" + i + ",'name"+i+"')");
            // 观察连接关闭与不关闭的输出情况，可以看到，不关闭连接的情况下，连接池最终会超时抛出异常
            // ... SQLTransientConnectionException : HikariPool-1 - Connection is not available, request timed out after 30001ms.
            // connection.close();
        }
    }
}
