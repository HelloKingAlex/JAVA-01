package com.alex.homework.beans;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author Alex Shen
 */
public class BeanDemo implements IBeanDemo {
    @Autowired
    BeanByAutoWired beanByAutoWired;

    @Autowired
    BeanByAutoWired beanByAutoWired2;

    @Resource(name = "beanByResource")
    BeanByResource beanByResource;
    @Resource(name = "beanByResource2")
    BeanByResource beanByResource2;
    @Override
    public void print() {
        // AutoWare针对的是类型装配
        System.out.println("=====Autowired=====");
        System.out.println(beanByAutoWired.toString());
        System.out.println(beanByAutoWired2.toString());
        beanByAutoWired.setId(7890);
        System.out.println(beanByAutoWired.toString());
        System.out.println(beanByAutoWired2.toString());

        // Resource 针对的是bean的id(name)装配
        System.out.println("=====Resource=====");
        System.out.println(beanByResource.toString());
        System.out.println(beanByResource2.toString());
        beanByResource2.setId(7890);
        System.out.println(beanByResource.toString());
        System.out.println(beanByResource2.toString());
    }
}
