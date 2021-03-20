package com.alex.homework.rpcfx.api;

import lombok.Data;

/**
 * RPC 应答
 *
 * @author Alex Shen
 */
@Data
public class RpcfxResponse {
    private Object result;
    private boolean status;
    private Exception exception;
}
