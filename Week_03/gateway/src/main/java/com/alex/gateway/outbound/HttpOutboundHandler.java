package com.alex.gateway.outbound;

import com.alex.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpOutboundHandler {
    public void handle(FullHttpRequest request, ChannelHandlerContext context, HttpRequestFilter filter);
}
