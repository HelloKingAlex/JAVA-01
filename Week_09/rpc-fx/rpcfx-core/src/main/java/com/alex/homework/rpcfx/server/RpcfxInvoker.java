package com.alex.homework.rpcfx.server;

import com.alex.homework.rpcfx.api.RpcfxRequest;
import com.alex.homework.rpcfx.api.RpcfxResolver;
import com.alex.homework.rpcfx.api.RpcfxResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * RPC 服务端
 *
 * @author Alex Shen
 */
public class RpcfxInvoker {
    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        Class<?> serviceClass = request.getServiceClass();

        Object service = resolver.resolve(serviceClass);
        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams());

            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods())
                .filter(m -> methodName.equals(m.getName()))
                .findFirst()
                .get();
    }
}
