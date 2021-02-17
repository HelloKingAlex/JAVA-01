package com.alex.homework.school;

import com.alex.homework.school.beans.Klass;
import com.alex.homework.school.beans.School;
import com.alex.homework.school.beans.Student;
import com.alex.homework.sclool.SchoolApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Shen
 */
@Configuration
@ConditionalOnClass(SchoolApplicationRunner.class)
public class SchoolAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(SchoolApplicationRunner.class)
    public SchoolApplicationRunner greetingApplicationRunner() {
        return new SchoolApplicationRunner();
    }

    @ConditionalOnMissingBean(Student.class)
    @Bean(name = "student100")
    public Student initStudent() {
        int id = (int)(Math.random() * 100) + 100;
        return new Student(id, "student" + id);
    }

    @ConditionalOnMissingBean(Klass.class)
    @Bean
    public Klass initClass01() {

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            students.add(new Student(i, "student l-" + i));
        }
        return new Klass(students);
    }

    @ConditionalOnMissingBean(School.class)
    @Bean
    public School initSchool() {
        return new School();
    }
}
