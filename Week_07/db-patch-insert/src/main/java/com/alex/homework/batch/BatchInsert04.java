package com.alex.homework.batch;

import com.alex.homework.sql.util.DataSourceHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用 jdbc,batchInsert, prepare statement
 * 每一批次插入10000条数据
 *
 * @author Alex Shen
 */
public class BatchInsert04 {
    public static void main(String[] args) throws SQLException {
        DataSourceHelper dataSourceHelper = new DataSourceHelper();
        HikariDataSource ds = dataSourceHelper.getDataSource();
        System.out.println("start at:" + System.currentTimeMillis());
        final long COUNTS = 10_0000;

        String sql = "insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values(?,?,1.00,0.00,'F')";
        PreparedStatement statement = ds.getConnection().prepareStatement(sql);
        for (long i = 1; i <= COUNTS; i++) {
            statement.setLong(1,i);
            statement.setLong(2,i);
            statement.addBatch();
        }
        statement.executeBatch();
        System.out.println("end at:" + System.currentTimeMillis());
        ds.close();
    }
}
