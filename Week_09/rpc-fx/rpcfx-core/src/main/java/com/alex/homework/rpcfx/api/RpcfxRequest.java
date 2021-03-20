package com.alex.homework.rpcfx.api;

import lombok.Data;

/**
 * RPC 请求
 *
 * @author Alex Shen
 */
@Data
public class RpcfxRequest {
    private Class<?> serviceClass;
    private String method;
    private Object[] params;
}
