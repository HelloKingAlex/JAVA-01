package com.alex.homework.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka 生产者
 *
 * @author Alex Shen
 */
@RestController
public class KafkaProducer {
    @Autowired
    private KafkaTemplate template;

    @RequestMapping("/produce")
    public String produce(String topic, String message){
        template.send(topic,message);
        return "message sent";
    }
}
