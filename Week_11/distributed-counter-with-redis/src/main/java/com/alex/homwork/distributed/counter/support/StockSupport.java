package com.alex.homwork.distributed.counter.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 库存支持类
 *
 * @author Alex Shen
 */
@Component
public class StockSupport {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisLockSupport lockSupport;

    private final String STOCK_KEY = "stock";
    public void initStock(Long stock) {
        lockSupport.unlock(STOCK_KEY);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(STOCK_KEY, stock);
    }

    public void decreaseStock() throws Exception {
        String lockKey = "lock-stock";
        boolean lock = lockSupport.tryLock(lockKey);

        if (lock) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Long stock = (Long) valueOperations.get(STOCK_KEY);
            System.out.println("current stock:" + stock);
            if( stock <= 0) {
                System.out.println("empty stock");
                lockSupport.unlock(lockKey);
                return;
            }
            valueOperations.set(STOCK_KEY, stock-1);
        }
        lockSupport.unlock(lockKey);
    }

    public Long getStock() throws Exception {
        String lockKey = "lock-stock";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long stock = (Long) valueOperations.get(STOCK_KEY);
        return stock;
    }
}
