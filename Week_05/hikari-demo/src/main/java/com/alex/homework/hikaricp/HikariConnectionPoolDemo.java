package com.alex.homework.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alex Shen
 */
public class HikariConnectionPoolDemo {
    public static void main(String[] args) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:~/h2/hikaricp-demo");
        //config.setDriverClassName(jdbcx.JdbcDataSource.class);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize","20");
        config.addDataSourceProperty("preStmtCacheSqlLimit","2048");

        HikariDataSource ds = new HikariDataSource(config);
        ds.getConnection()
                .createStatement()
                .execute("drop table person");
        ds.getConnection()
                .createStatement()
                .execute("create table person(id integer primary key,name varchar(20))");
        AtomicInteger i = new AtomicInteger(0);

        while (i.get() < 1000) {
            new Thread(()-> {
                try {
                    ds.getConnection()
                            .createStatement()
                            .execute("insert into person values(" + i.get() + ",'name"+i.get()+"')");
                    System.out.println("connections:"+i.incrementAndGet());
                    Thread.sleep(3000);
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                    return;
                }
            }).start();
        }
    }
}
