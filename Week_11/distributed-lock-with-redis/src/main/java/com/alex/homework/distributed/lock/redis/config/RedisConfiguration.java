package com.alex.homework.distributed.lock.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.ObjectStreamField;

/**
 * redis 客户端配置类
 *
 * @author Alex Shen
 */
@Configuration
@ConditionalOnMissingBean(StringRedisTemplate.class)
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        // 这里只是简单的演示了用户自己配置 RedisTemplate 的入口
        // 具体做法可以参考 StringRedisTemplate 的 实现
        RedisTemplate<String, String> template = new StringRedisTemplate(factory);
        return  template;
    }
}
