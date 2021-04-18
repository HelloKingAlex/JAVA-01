package com.alex.homework.simple.mq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * 消息实体
 *
 * headers 里将存储一些便于后期处理的键值对
 *
 * @author Alex Shen
 */
@AllArgsConstructor
@Data
public class Message<T> {
    private HashMap<String, Object> headers;
    private T body;
}
