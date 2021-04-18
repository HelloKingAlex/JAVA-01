package com.alex.homework.simple.mq.core;

import com.alex.homework.simple.mq.entity.Message;
import com.alex.homework.simple.mq.exception.SimpleMessageQueueException;

/**
 * 消息 消费者
 *
 * @author Alex Shen
 */
public class Consumer<T> {
    private final Broker broker;
    private SimpleMessageQueue queue;
    private final int id;
    public Consumer(Broker broker, int id) {
        this.broker = broker;
        this.id = id;
        System.out.println("Consumer[" + this.id + "]");
    }

    public int getId() {
        return this.id;
    }
    public void subscribe(String topic) throws SimpleMessageQueueException {
        this.queue = this.broker.selectQueue(topic);
        if (null == this.queue) {
            throw new SimpleMessageQueueException("Topic[" + topic + "] 不存在，无法订阅 topic ");
        }
    }

    public Message<T> poll(long timeout) {
        int offset = this.broker.getConsumerOffset(this.id);
        return poll(offset, timeout);
    }
    public Message<T> poll(int offset, long timeout) {
        int size = this.queue.size();
        if(this.queue.size() <= 0 || offset > size-1) {
            return null;
        }
        Message message = queue.poll(offset, timeout);
        if (null != message) {
            this.broker.increaseOffset(this.id);
        }
        return message;
    }
}
