package com.alex.homework.simple.mq.core;

import com.alex.homework.simple.mq.entity.Message;
import com.alex.homework.simple.mq.exception.SimpleMessageQueueException;

/**
 * 消息生产者
 *
 * @author Alex Shen
 */
public class Producer {
    private final Broker broker;

    public Producer(Broker broker) {
        this.broker = broker;
    }

    public boolean send(String topic, Message message) throws SimpleMessageQueueException {
        SimpleMessageQueue queue = this.broker.selectQueue(topic);
        if (queue == null) {
            throw new SimpleMessageQueueException("topic:[" + topic + "] 不存在，无法发送消息");
        }


        return queue.send(message);
    }
}
