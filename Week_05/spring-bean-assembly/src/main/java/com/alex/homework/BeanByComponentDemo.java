package com.alex.homework;

import com.alex.homework.components.BeanByComponent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * @author Alex Shen
 */
public class BeanByComponentDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.alex.homework.components");
        // 这里必须先refresh一下
        // BeanByComponent beanByComponent = (BeanByComponent)context.getBean("beanByComponent");
        context.refresh();

        BeanByComponent beanByComponent = (BeanByComponent)context.getBean("beanByComponent");
        beanByComponent.setId(9527);
        beanByComponent.setName("this is beanByComponent");
        System.out.println(beanByComponent.toString());

        // @Repository , @ Controller , @Service , @Configration 都可以看做是Component的泛化
    }
}
