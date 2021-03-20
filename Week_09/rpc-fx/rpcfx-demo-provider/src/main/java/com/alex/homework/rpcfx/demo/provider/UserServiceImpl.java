package com.alex.homework.rpcfx.demo.provider;

import com.alex.homework.rpcfx.demo.api.User;
import com.alex.homework.rpcfx.demo.api.UserService;

/**
 * TODO
 *
 * @author Alex Shen
 */
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "alex-" + System.currentTimeMillis());
    }
}
