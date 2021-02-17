package com.alex.homework.config;

import com.alex.homework.beans.BeanByConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alex Shen
 */
@Configuration
public class BeanConfig {
    @Bean("beanByConfiguration")
    public BeanByConfiguration init() {
        BeanByConfiguration instance = new BeanByConfiguration();
        instance.setId(9527);
        instance.setName("this is beanByConfiguration");
        return instance;
    }
}
