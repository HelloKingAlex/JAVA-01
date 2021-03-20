package com.alex.homework.rpcfx.api;

import lombok.Builder;
import lombok.Data;

/**
 * 服务描述
 *
 * @author Alex Shen
 */
@Data
@Builder
public class ServiceProviderDesc {
    private String host;
    private Integer port;
    private String serviceClass;
}
