package com.alex.homwork.distributed.counter;

import com.alex.homwork.distributed.counter.support.StockSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Alex Shen
 */
@SpringBootApplication
public class DistributedCounterWithRedisApplication implements ApplicationRunner {

    @Autowired
    StockSupport stockSupport;

    public static void main(String[] args) {
        SpringApplication.run(DistributedCounterWithRedisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int threadMaxCount = 1002;
        stockSupport.initStock(1000L);
        CountDownLatch countDownLatch = new CountDownLatch(threadMaxCount);
        Runnable runnable = () -> {
            try {
                stockSupport.decreaseStock();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        };

        ExecutorService es = Executors.newFixedThreadPool(8);
        for (int i = 0; i < threadMaxCount; i++) {
            es.submit(runnable);
        }
        countDownLatch.await();
        es.shutdown();
        System.out.println("final stock is " + stockSupport.getStock());

    }
}
