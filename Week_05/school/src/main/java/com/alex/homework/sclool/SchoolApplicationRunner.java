package com.alex.homework.sclool;

/**
 * @author Alex Shen
 */
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
public class SchoolApplicationRunner implements ApplicationRunner{
    public SchoolApplicationRunner() {
        System.out.println("===初始化 SchoolApplicationRunner");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("===SchoolApplicationRunner 运行");
    }
}
