package com.alex.homework.school.beans;
import java.io.Serializable;

/**
 * @author Alex Shen
 */

public class Student implements Serializable {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "student :" + this.id + " name :" + this.name;
    }
}
