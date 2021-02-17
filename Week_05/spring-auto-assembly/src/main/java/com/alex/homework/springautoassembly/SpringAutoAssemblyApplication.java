package com.alex.homework.springautoassembly;

import com.alex.homework.school.beans.School;
import com.alex.homework.sclool.SchoolApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author Alex Shen
 */
@SpringBootApplication
public class SpringAutoAssemblyApplication extends SchoolApplicationRunner {

    @Autowired
    School school;
    public static void main(String[] args) {
        SpringApplication.run(SpringAutoAssemblyApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        super.run(args);
        school.ding();
        school.getClass1().dong();
    }
}
