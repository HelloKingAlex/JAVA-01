package com.alex.homework.batch;

import com.alex.homework.sql.util.DataSourceHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用 jdbc,逐条插入
 *
 * @author Alex Shen
 */
public class BatchInsert01 {
    public static void main(String[] args) throws SQLException {
        DataSourceHelper dataSourceHelper = new DataSourceHelper();
        HikariDataSource ds = dataSourceHelper.getDataSource();
        System.out.println("start at:" + System.currentTimeMillis());
        final long COUNTS = 10_0000;
        Statement statement = ds.getConnection().createStatement();
        for (int i = 1; i <= COUNTS; i++) {
            String sql = "insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values("+ i +"," + i+ ",1.00,0.00,'F')";
            statement.execute(sql);
        }
        System.out.println("end at:" + System.currentTimeMillis());
        ds.close();
    }
}
