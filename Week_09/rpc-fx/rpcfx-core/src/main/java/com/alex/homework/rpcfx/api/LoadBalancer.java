package com.alex.homework.rpcfx.api;

import java.util.List;

/**
 * 负载均衡
 *
 * @author Alex Shen
 */
public interface LoadBalancer {
    String select(List<String> urls);
}
