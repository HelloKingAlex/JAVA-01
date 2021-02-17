package com.alex.homework.school.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Alex Shen
 */
@Component
public class School {
    @Autowired
    Klass class1;

    @Resource(name = "student100")
    Student student100;

    public School(){}
    public School(Klass klass, Student student) {
        this.class1 = klass;
        this.student100 = student;
    }

    public Klass getClass1() {
        return this.class1;
    }
    public void ding(){
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
    }
}
