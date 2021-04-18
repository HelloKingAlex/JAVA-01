package com.alex.homework.simple.mq.core;

import com.alex.homework.simple.mq.exception.SimpleMessageQueueException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单消息 Broker
 *
 * @author Alex Shen
 */
public final class Broker {
    public static final int CAPACITY = 10000;
    private AtomicInteger ids = new AtomicInteger(0);

    private final Map<Integer, Integer> offsets = new ConcurrentHashMap<>(64);
    private final Map<String, SimpleMessageQueue> queues = new ConcurrentHashMap<>(64);

    public void createTopic(String topic) throws SimpleMessageQueueException{
        if (existsTopic(topic)) {
            throw new SimpleMessageQueueException("topic:[" + topic + "] 已存在，请不要重复创建");
        }
        queues.put(topic, new SimpleMessageQueue(topic,CAPACITY));
    }

    public boolean existsTopic(String topic) {
        return queues.containsKey(topic);
    }

    public SimpleMessageQueue selectQueue(String topic) {
        return this.queues.get(topic);
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public int getConsumerOffset(int id) {
        return offsets.getOrDefault(id,0);
    }

    public void increaseOffset(int id) {
        offsets.put(id,offsets.getOrDefault(id,0) + 1);
    }
    synchronized
    public Consumer createConsumer(int id) {
        if (!offsets.containsKey(id)) {
            offsets.put(id, 0);
        }
        return new Consumer(this,id);
    }
    public Consumer createConsumer() {
        return new Consumer(this,ids.incrementAndGet());
    }
}
