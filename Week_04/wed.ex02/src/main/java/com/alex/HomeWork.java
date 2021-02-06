package com.alex;

import sun.awt.windows.ThemeReader;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeWork {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 方法1，使用Future接口
        useFutureByThreadPool(5);
        // 方法2. 使用FutureTask接口
        useFutureBySingleThread(6);
        // 方法3. 使用CompletableFuture 消费方式
        useCompletableFutureConsume(7);
        // 方法4. 使用CompletableFuture join
        useCompletableFutureJoin(8);
        // 方法5. 使用CompletableFuture 竞争模式
        useCompletableFutureCompete(9);
        // 方法6. 使用CountDownLatch
        useCountDownLatch(10);
        // 方法7. 使用CyclicBarrier
        useCycleBarrier(11);
        // 方法8. 线程中断
        useInterrupted(12);
    }

    private static void useFutureByThreadPool(int n) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return fab(n);
            }
        });
        executor.shutdown();
        System.out.println(future.get());
    }

    private static void useFutureBySingleThread(int n) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return fab(n);
            }
        });
        new Thread(task).start();
        System.out.println(task.get());
    }

    private static void useCompletableFutureConsume(int n) {
        CompletableFuture
                .supplyAsync(() -> {return fab(n);})
                .thenAccept(v -> {System.out.println(v);});
    }

    private static void useCompletableFutureJoin(int n) {
        int result = CompletableFuture
                .supplyAsync(() -> {return fab(n);})
                .join();
        System.out.println(result);
    }

    private static void useCompletableFutureCompete(int n) {
        String result = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep((int)(Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "threadA:" + fab(n);
                }).applyToEither(CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep((int)(Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "threadB:" + fab(n);
                }),(s) -> {return s;}).join();
        System.out.println(result);
    }

    private static void useCountDownLatch(int n) throws InterruptedException, ExecutionException {
        AtomicInteger result = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(()->{
            result.set(fab(n));
            latch.countDown();
        }).start();
        latch.await();
        System.out.println(result);
    }

    private static void useCycleBarrier(int n) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, ()->{
                System.out.println(fab(n));
        });
        new Thread(() -> {
            try {
                System.out.println("count:" + 1);
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                System.out.println("count:" + 2);
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void useInterrupted(int n) {
        Thread th1 = new Thread(()->{
            try {
                while (true) {
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted:" + fab(n));
            }
        });
        th1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        th1.interrupt();
    }
    private static int fab(int n) {
        if ( n < 2) {
            return n;
        }
        int[] dp = new int[n+1];
        dp[1] = 1;
        dp[2] = 1;
        for ( int i = 3;i <=n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }
}
