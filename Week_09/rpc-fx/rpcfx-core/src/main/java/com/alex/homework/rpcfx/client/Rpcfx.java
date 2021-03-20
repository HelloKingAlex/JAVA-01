package com.alex.homework.rpcfx.client;

import com.alex.homework.rpcfx.api.Filter;
import com.alex.homework.rpcfx.api.RpcfxRequest;
import com.alex.homework.rpcfx.api.RpcfxResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author Alex Shen
 */
public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("com.alex.homework");
    }
    public static <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader()
                , new Class[]{serviceClass}
                , new RpcfxInvocationHandler(serviceClass, url, filters));
    }

    public static class RpcfxInvocationHandler implements InvocationHandler {
        private static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;
        private final Filter[] filters;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url, Filter... filters) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.filters = filters;
        }
        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
            String requestJson = JSON.toJSONString(req);
            System.out.println("request json: " + requestJson);

            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, requestJson))
                    .build();

            String responseJson = client.newCall(request).execute().body().string();
            System.out.println("response json: " + responseJson);
            return JSON.parseObject(responseJson, RpcfxResponse.class);
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass);
            request.setMethod(method.getName());
            request.setParams(params);

            if (null != filters) {
                for (Filter filter : filters) {
                    if (!filter.filter(request)) {
                        return null;
                    }
                }
            }

            RpcfxResponse response = post(request, url);
            return JSON.parse(response.getResult().toString());
        }
    }
}
