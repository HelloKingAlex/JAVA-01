package com.alex.homework.rpcfx.api;

/**
 * 过滤器
 *
 * @author Alex Shen
 */
public interface Filter {
    boolean filter(RpcfxRequest request);
}
