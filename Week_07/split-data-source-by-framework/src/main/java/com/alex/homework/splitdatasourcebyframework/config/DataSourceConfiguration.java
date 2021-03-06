package com.alex.homework.splitdatasourcebyframework.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.ReplicaQueryRuleConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.rule.ReplicaQueryDataSourceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * sharding sphere 配置
 *
 * @author Alex Shen
 */
@Configuration
public class DataSourceConfiguration {
    @Bean(name="dataSource")
    public DataSource dataSource() throws SQLException{
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        // 数据源
        HikariDataSource primaryDs = getDataSource01();
        HikariDataSource secondaryDs02 = getDataSource02();
        dataSourceMap.put("primary_ds", primaryDs);
        dataSourceMap.put("secondary_ds_01", secondaryDs02);

        // 读写分离规则
        List<ReplicaQueryDataSourceRuleConfiguration> configurations = new ArrayList<>();
        ReplicaQueryDataSourceRuleConfiguration rule = new ReplicaQueryDataSourceRuleConfiguration("ds", "primary_ds", Arrays.asList("secondary_ds_01"), "load_balancer");
        configurations.add(rule);

        Map<String, ShardingSphereAlgorithmConfiguration> loadBalancers = new HashMap<>(1);
        loadBalancers.put("load_balancer", new ShardingSphereAlgorithmConfiguration("ROUND_ROBIN", new Properties()));
        ReplicaQueryRuleConfiguration ruleConfiguration = new ReplicaQueryRuleConfiguration(configurations, loadBalancers);

        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Arrays.asList(ruleConfiguration), new Properties());
    }

    public HikariDataSource getDataSource01() {
        final String USER = "root";
        final String PASSWORD  = "123456";
        final String URL = "jdbc:mysql://localhost:33061/geektime_e_shop";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        return new HikariDataSource(config);
    }

    public HikariDataSource getDataSource02() {
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
