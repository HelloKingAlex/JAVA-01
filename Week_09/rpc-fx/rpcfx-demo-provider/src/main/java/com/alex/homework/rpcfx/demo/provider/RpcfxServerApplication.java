package com.alex.homework.rpcfx.demo.provider;

import com.alex.homework.rpcfx.api.RpcfxRequest;
import com.alex.homework.rpcfx.api.RpcfxResolver;
import com.alex.homework.rpcfx.api.RpcfxResponse;
import com.alex.homework.rpcfx.demo.api.UserService;
import com.alex.homework.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author Alex Shen
 */
@SpringBootApplication
@RestController
public class RpcfxServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RpcfxServerApplication.class, args);
    }

    @Autowired
    RpcfxInvoker invoker;

    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return invoker.invoke(request);
    }

    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver){
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver(){
        return new DemoResolver();
    }

    @Bean(name = "com.alex.homework.rpcfx.demo.api.UserService")
    public UserService createUserService(){
        return new UserServiceImpl();
    }
}
