package com.alex.homework.school.beans;

import java.util.List;

/**
 * @author Alex Shen
 */
public class Klass {
    List<Student> students;
    public Klass(List<Student> students) {
        this.students = students;
    }
    public List<Student> getStudents() {
        return this.students;
    }
    public void dong(){
        for (Student student : students) {
            System.out.println(student.toString());
        }
    }
}
