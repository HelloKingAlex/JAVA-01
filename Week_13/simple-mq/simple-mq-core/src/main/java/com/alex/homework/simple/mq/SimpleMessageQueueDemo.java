package com.alex.homework.simple.mq;

import com.alex.homework.simple.mq.core.Broker;
import com.alex.homework.simple.mq.core.Consumer;
import com.alex.homework.simple.mq.core.Producer;
import com.alex.homework.simple.mq.entity.Message;
import com.alex.homework.simple.mq.exception.SimpleMessageQueueException;
import lombok.SneakyThrows;

/**
 * 示例
 *
 * @author Alex Shen
 */
public class SimpleMessageQueueDemo {
    @SneakyThrows
    public static void main(String[] args) {
        String topic = "hello-queue";
        Broker broker = new Broker();
        broker.createTopic(topic);

        Consumer consumer1 = broker.createConsumer();
        Consumer consumer2 = broker.createConsumer();
        Consumer consumer3 = broker.createConsumer(consumer1.getId());
        consumer1.subscribe(topic);
        consumer2.subscribe(topic);
        consumer3.subscribe(topic);

        final boolean[] flag = new boolean[1];
        flag[0] = true;

        new Thread(() -> {
            int counts = 50;
            while (flag[0] && counts > 0) {
                Message<String> message = consumer1.poll(100);
                if (null != message) {
                    System.out.println("consumer1:"+ message.getBody());
                }
            }
            System.out.println("consumer1:退出");
        }).start();

        new Thread(() -> {
            while (flag[0]) {
                Message<String> message = consumer2.poll(100);
                if (null != message) {
                    System.out.println("consumer2:"+ message.getBody());
                }
            }
            System.out.println("consumer2:退出");
        }).start();

        Producer producer = broker.createProducer();

        System.out.println("开始生产消息");
        for (int i = 0; i < 1000; i++) {
            producer.send(topic,new Message(null, "message["+i+"]"));
        }
        System.out.println("生产消息完毕");
        Thread.sleep(3000);
        new Thread(() -> {
            int offset = 55;
            while (flag[0]) {
                Message<String> message = consumer3.poll(offset++, 100);
                if (null != message) {
                    System.out.println("consumer3:"+ message.getBody());
                }
            }
            System.out.println("consumer3:退出");
        }).start();
        Thread.sleep(3000);
        flag[0] = false;
    }
}
