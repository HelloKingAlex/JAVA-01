package com.alex.homework.rpcfx.demo.consumer.controller;

import com.alex.homework.rpcfx.annotation.Remote;
import com.alex.homework.rpcfx.demo.api.User;
import com.alex.homework.rpcfx.demo.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author Alex Shen
 */
@RestController
public class UserController {
    @Remote(url="http://localhost:8080/")
    private UserService userService;

    @RequestMapping("/user")
    private void showUser() {
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());
    }
}
