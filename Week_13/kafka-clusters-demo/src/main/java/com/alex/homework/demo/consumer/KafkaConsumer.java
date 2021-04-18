package com.alex.homework.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka 消费端
 *
 * @author Alex Shen
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"test32"})
    public void consume(ConsumerRecord record){
        System.out.println(record.topic()+":"+record.value());
    }
}
