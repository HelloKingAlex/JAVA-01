package com.alex.homework;

import com.alex.homework.beans.IBeanDemo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Alex Shen
 */
public class BeanByXmlAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beanConfiguration.xml");
        IBeanDemo beanDemo = context.getBean(IBeanDemo.class);
        beanDemo.print();
    }
}
