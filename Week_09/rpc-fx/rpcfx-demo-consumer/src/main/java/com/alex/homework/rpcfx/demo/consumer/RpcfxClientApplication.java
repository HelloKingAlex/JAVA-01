package com.alex.homework.rpcfx.demo.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.swing.*;

/**
 * TODO
 *
 * @author Alex Shen
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class RpcfxClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpcfxClientApplication.class, args);
    }
}
