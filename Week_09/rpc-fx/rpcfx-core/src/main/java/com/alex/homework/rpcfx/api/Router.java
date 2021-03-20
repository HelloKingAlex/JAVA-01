package com.alex.homework.rpcfx.api;

import java.util.List;

/**
 * 路由
 *
 * @author Alex Shen
 */
public interface Router {
    List<String> route(List<String> urls);
}
