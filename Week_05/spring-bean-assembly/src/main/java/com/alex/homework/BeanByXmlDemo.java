package com.alex.homework;

import com.alex.homework.beans.BeanByXml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Alex Shen
 */
public class BeanByXmlDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beanConfiguration.xml");
        BeanByXml beanByXml = (BeanByXml) context.getBean("beanByXml");
        System.out.println(beanByXml.toString());
    }
}
