package com.alex.homework.week08sun02.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据源配置
 *
 * @author Alex Shen
 */
@Configuration
public class DataSourceConfiguration {

    public DataSource db01() {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl("jdbc:mysql://localhost:33070/geektime_e_shop_01");
        configuration.setUsername("root");
        configuration.setPassword("123456");
        return new HikariDataSource(configuration);
    }

    public DataSource db02() {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl("jdbc:mysql://localhost:33070/geektime_e_shop_02");
        configuration.setUsername("root");
        configuration.setPassword("123456");
        return new HikariDataSource(configuration);
    }

    @Bean(name="dataSource")
    public DataSource dataSource() throws SQLException {
        // 1. 设置数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("ds_0",db01());
        dataSourceMap.put("ds_1",db02());
        // 2. 设置配置内容
        RuleConfiguration ruleConfiguration = initRuleConfiguration();
        // 3. 启用设置
        Properties otherProperties = new Properties();
        otherProperties.setProperty("sql-show", "true");
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(ruleConfiguration), otherProperties);
    }

    private RuleConfiguration initRuleConfiguration() {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "dbShardingAlgorithm"));

        // 表 orders 分库分表策略
        shardingRuleConfig.getTables().add(shardingOrdersRuleConfig());
        // 分库算法
        shardingRuleConfig.getShardingAlgorithms().put("dbShardingAlgorithm", initShardingDBRule());
        // 分表算法
        shardingRuleConfig.getShardingAlgorithms().put("tableShardingAlgorithm", initShardingTableRule());

        return shardingRuleConfig;
    }

    private ShardingTableRuleConfiguration shardingOrdersRuleConfig() {
        ShardingTableRuleConfiguration shardingTableOrdersRuleConfig =
                new ShardingTableRuleConfiguration("orders", "ds_${0..1}.orders${0..15}");
        shardingTableOrdersRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "dbShardingAlgorithm"));
        shardingTableOrdersRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "tableShardingAlgorithm"));
        return shardingTableOrdersRuleConfig;
    }

    private ShardingSphereAlgorithmConfiguration initShardingDBRule() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression","ds_${user_id % 2}");
        return new ShardingSphereAlgorithmConfiguration("INLINE", props);
    }

    private ShardingSphereAlgorithmConfiguration initShardingTableRule() {
        Properties props = new Properties();
        props.setProperty("algorithm-expression","orders${id % 16}");
        return new ShardingSphereAlgorithmConfiguration("INLINE", props);
    }
}
