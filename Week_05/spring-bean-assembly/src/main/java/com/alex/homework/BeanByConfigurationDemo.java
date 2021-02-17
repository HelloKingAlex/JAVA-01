package com.alex.homework;

import com.alex.homework.beans.BeanByConfiguration;
import com.alex.homework.config.BeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Alex Shen
 */
public class BeanByConfigurationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
        BeanByConfiguration beanByConfiguration = context.getBean(BeanByConfiguration.class);
        System.out.println(beanByConfiguration.toString());
        beanByConfiguration.setId(1234);

        BeanByConfiguration beanByConfiguration2 = (BeanByConfiguration)context.getBean("beanByConfiguration");
        System.out.println(beanByConfiguration2.toString());
    }
}
