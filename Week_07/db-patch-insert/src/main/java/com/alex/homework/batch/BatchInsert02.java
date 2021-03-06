package com.alex.homework.batch;

import com.alex.homework.sql.util.DataSourceHelper;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用 jdbc,batchInsert
 * 每一批次插入10000条数据
 *
 * @author Alex Shen
 */
public class BatchInsert02 {
    public static void main(String[] args) throws SQLException {
        DataSourceHelper dataSourceHelper = new DataSourceHelper();
        HikariDataSource ds = dataSourceHelper.getDataSource();
        System.out.println("start at:" + System.currentTimeMillis());
        final long COUNTS = 10_0000;
        final long PATCH_COUNTS = 10_0000;
        long round = COUNTS / PATCH_COUNTS;
        Statement statement = ds.getConnection().createStatement();

        for (int i = 0; i < round; i++) {
            for(int j = 1; j <=PATCH_COUNTS; j++ ) {
                long index = i * PATCH_COUNTS +j;
                String sql = "insert into geektime_e_shop.order(id,user_id,total_price,total_discount,status) values("+ index  +"," + index+ ",1.00,0.00,'F')";
                statement.addBatch(sql);
            }

            statement.executeBatch();
        }
        System.out.println("end at:" + System.currentTimeMillis());
        ds.close();
    }
}
