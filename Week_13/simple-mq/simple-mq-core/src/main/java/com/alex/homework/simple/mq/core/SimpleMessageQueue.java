package com.alex.homework.simple.mq.core;

import com.alex.homework.simple.mq.entity.Message;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 简单消息队列 核心
 *
 * @author Alex Shen
 */
public class SimpleMessageQueue {
    private String topic;
    private int capacity;
    private ArrayList<Message> queue;

    public SimpleMessageQueue(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new ArrayList<>(capacity);;
    }

    public boolean send(Message message) {
        boolean result = false;
        synchronized (this) {
            result = queue.add(message);
        }
        return result;
    }

    public Message poll(int offset) {
        return queue.get(offset);
    }

    @SneakyThrows
    public Message poll(int offset, long timeout) {
        long current = System.currentTimeMillis();
        long end = current + timeout;
        while (end >= current) {
            Message message = poll(offset);
            if (message == null) {
                Thread.sleep(10);
            } else {
                return message;
            }
        }
        return null;
    }

    public int size() {
        return this.queue.size();
    }
}
