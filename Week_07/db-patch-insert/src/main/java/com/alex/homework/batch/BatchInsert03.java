package com.alex.homework.batch;

import com.alex.homework.sql.util.DataSourceHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用 jdbc,多条模式
 * 每一批次插入100_0000条数据
 *
 * @author Alex Shen
 */
public class BatchInsert03 {
    public static void main(String[] args) throws SQLException {
        DataSourceHelper dataSourceHelper = new DataSourceHelper();
        HikariDataSource ds = dataSourceHelper.getDataSource();
        System.out.println("start at:" + System.currentTimeMillis());
        final long COUNTS = 100_0000;
        Statement statement = ds.getConnection().createStatement();

        StringBuilder sql = new StringBuilder("insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values");
        for (long i = 1; i <= COUNTS - 1; i++) {
            sql.append("("+ i  +"," + i+ ",1.00,0.00,'F'),");
        }
        sql.append("("+ COUNTS  +"," + COUNTS+ ",1.00,0.00,'F')");
        statement.execute(sql.toString());
        System.out.println("end at:" + System.currentTimeMillis());
        ds.close();
    }
}
