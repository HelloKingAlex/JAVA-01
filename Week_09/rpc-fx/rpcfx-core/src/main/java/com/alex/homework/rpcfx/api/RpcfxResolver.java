package com.alex.homework.rpcfx.api;

/**
 * RPC 解析
 *
 * @author Alex Shen
 */
public interface RpcfxResolver {
    Object resolve(Class<?> serviceClass);
}
