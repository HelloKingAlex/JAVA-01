package com.alex.homework.preparestatement;

import java.sql.*;
import java.util.Properties;

/**
 * @author Alex Shen
 */
public class PrepareStatementDemo {
    public static void main(String[] args) throws SQLException {
        Connection conn1 = getConnection();
        Connection conn2 = getConnection();
        // 使用批处理Insert
        // 关闭自动提交，先不自动提交或回滚事务,便于演示
        conn1.setAutoCommit(false);
        // 建表
        initEmbedDB(conn1);
        // 批处理插入一些演示数据
        showBatchInsert(conn1,0,4);

        System.out.println("===after batch insert ===");
        System.out.println("===事务未提交，观察不同connection下获取数据的情况 ===");
        System.out.println("===conn1 ===");
        showData(conn1);
        System.out.println("===conn2 ===");
        showData(conn2);

        // 提交事务
        conn1.commit();
        System.out.println("===事务提交后，观察不同connection下获取数据的情况 ===");
        System.out.println("===conn1 ===");
        showData(conn1);
        System.out.println("===conn2 ===");
        showData(conn2);

        // 事务回滚
        System.out.println("=========================");
        System.out.println("===演示事务回滚 ===");
        showBatchInsert(conn1,4,2);
        System.out.println("===after batch insert before savepoint===");
        showData(conn1);
        // 创建安全点
        Savepoint save1 = conn1.setSavepoint();

        showBatchInsert(conn1,6,2);
        System.out.println("===after batch insert after savepoint===");
        showData(conn1);

        conn1.rollback(save1);
        System.out.println("===after rollback to savepoint===");
        showData(conn1);

        System.out.println("===conn1 commit===");
        conn1.commit();
        showData(conn1);


        // prepared statement
        System.out.println("=========================");
        System.out.println("===演示PreparedStatement ===");
        // 还原自动提交设置
        conn1.setAutoCommit(true);
        try (PreparedStatement preparedStatement = conn1.prepareStatement("insert into person values(?,?)") ) {
            preparedStatement.setInt(1,100);
            preparedStatement.setString(2,"prepared");
            preparedStatement.execute();
            System.out.println("===after insert===");
            showData(conn1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("===演示PreparedStatement 防注入===");
        String attackWords = "prepared' or '1'='1";
        try (PreparedStatement preparedStatement = conn1.prepareStatement("update person set name=? where name=?") ) {
            preparedStatement.setString(1,"attack");
            preparedStatement.setString(2,attackWords);
            preparedStatement.execute();
            System.out.println("===after attack===");
            showData(conn1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("===演示Statement 被攻击===");
        try (Statement attack = conn1.createStatement()) {
            String attackWording = "update person set name='attack' where name='" + attackWords +"'";
            attack.execute(attackWording);
            System.out.println("===after attack===");
            showData(conn1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn1.close();
        conn2.close();
    }

    public static void showData(Connection conn) throws SQLException {
        String query = "select * from person where 1=1";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            System.out.println("name:" + rs.getString("name"));
        }
    }
    private static void showBatchInsert(Connection conn, int start, int count) throws SQLException {
        System.out.println("===try to insert "+ count +" rows into table person");
        try (Statement statementInsert = conn.createStatement()) {
            // 官方建议批处理适合增删改，不适合查。
            // 实际上应该也不会有批处理的查才对
            for (int i = start; i < start + count; i++) {
                statementInsert.addBatch("insert into person values(" + i + ",'name"+i+"')");
            }

            statementInsert.executeBatch();
            // conn.commit();
        } catch (BatchUpdateException e) {
            e.printStackTrace();
        } finally {
            // 还原自动提交设置
            // conn.setAutoCommit(true);
        }
    }
    private static Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "sa");
        connectionProps.put("password", "1111");
        // embeded
        conn = DriverManager.getConnection("jdbc:h2:~/h2/prepare-statement-demo",connectionProps);
        // in-memory
        // conn = DriverManager.getConnection("jdbc:h2:mem:prepare-statement-demo",connectionProps);
        return conn;
    }

    public static void initEmbedDB(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        System.out.println("===create table person");
        statement.execute("drop table if exists person");
        statement.execute("create table person(id integer primary key,name varchar(20))");
    }
}
