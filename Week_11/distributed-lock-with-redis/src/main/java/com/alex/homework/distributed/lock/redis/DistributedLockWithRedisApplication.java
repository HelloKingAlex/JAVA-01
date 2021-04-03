package com.alex.homework.distributed.lock.redis;

import com.alex.homework.distributed.lock.redis.support.RedisLockSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Alex Shen
 */
@SpringBootApplication
public class DistributedLockWithRedisApplication implements ApplicationRunner {

    @Autowired
    RedisLockSupport lockSupport;
    public static void main(String[] args) {
        SpringApplication.run(DistributedLockWithRedisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String lock = "distributed-lock";
        lockSupport.unlock(lock);
        Runnable runnable = () -> {
            try {
                if (lockSupport.tryLock(lock)) {
                    System.out.println(Thread.currentThread().getName() + ":lock succeed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable,"thread-0001").start();
        new Thread(runnable,"thread-0002").start();
    }
}
