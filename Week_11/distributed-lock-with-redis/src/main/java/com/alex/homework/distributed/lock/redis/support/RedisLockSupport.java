package com.alex.homework.distributed.lock.redis.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 使用Redis作为分布式锁的工具类
 *
 * @author Alex Shen
 */
@Component
public class RedisLockSupport {
    @Autowired
    private RedisTemplate redisTemplate;

    private final Long TIME_OUT = 3_000L;

    public boolean tryLock(String lock) throws Exception{
        Long current = System.currentTimeMillis();
        Long end = current + TIME_OUT;
        // 尝试加锁的时长为3秒
        while( current < end) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            // 设置加锁过期时间为 1 分钟
            boolean success = valueOperations.setIfAbsent(lock, "locked",1, TimeUnit.MINUTES);
            if (success) {
                return true;
            }
            // 演示用
            System.out.println(Thread.currentThread().getName() + ":try next round");
            Thread.sleep(10);
            current = System.currentTimeMillis();
        }
        throw new Exception("Lock Timeout");
    }

    public void unlock(String lock) {
        redisTemplate.delete(lock);
    }
}
