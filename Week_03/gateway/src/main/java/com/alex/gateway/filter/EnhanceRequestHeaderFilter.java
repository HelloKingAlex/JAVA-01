package com.alex.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class EnhanceRequestHeaderFilter implements HttpRequestFilter{
    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext ctx) {
        request.headers().set("goal", "survival");
    }
}
