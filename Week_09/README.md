# 学习笔记

## 作业

### 周三

#### ex03 改造自定义 RPC 的程序

* [尝试将服务端写死查找接口实现类变成泛型和反射](rpc-fx/rpcfx-core/src/main/java/com/alex/homework/rpcfx/server/RpcfxInvoker.java)
* [尝试将客户端动态代理改成 AOP，添加异常处理](rpc-fx/rpcfx-demo-consumer/src/main/java/com/alex/homework/rpcfx/demo/consumer/controller/UserController.java)
* [尝试使用 Netty+HTTP 作为 client 端传输方式(not finished)]()

### 周日

#### ex03 结合 dubbo+hmily，实现一个 TCC 外汇交易处理(not finished)

## 笔记

### RPC-远程过程调用

分布式系统的两大基石: RPC 与 MQ

RPC 表示两个系统间，直连的密集型的通信

MQ 指的是异步的，通过第三方中间件的通信方式

#### RPC的构成

* 存根进程 stub
* 负责网络传输的运行时库 RPC runtime
* 通过二进制传输的数据的编解码

#### RPC 定义

像调用本地方法一样调用远程方法

例子

```java
// 本地的
UserService service = new UserService();
User user = service.findById(1);

// RPC
UserService service = Rpcfx.create(UserService.class, url);
User user = service.findById(1);
```

#### RPC原理

1. 本地代理存根: Stub
2. 本地序列化反序列化
3. 网络通信
4. 远程序列化反序列化
5. 远程服务存根：Skeleton
6. 调用实际业务服务
7. 原路返回服务结果
8. 返回给本地调用方

##### RPC原理-设计

RPC 是基于接口的远程服务调用

远程及本地需要共享: POJO 实体类的定义，接口的定义

跨平台的RPC框架，如果需要实现语言无关，是需要实现用于描述接口本身内容的，比如 WSDL, WADL, IDL

从角色上来看:

* 远程:服务提供者
* 本地:服务消费者

##### RPC原理-代理

Java体系下，代理可以选择动态代理，或者字节码增强技术

##### RPC原理-序列化

1. 语言原生的序列化， RMI， Remoting
2. 二进制平台无关的技术， Hessian, avro, kyro, fst等
3. 文本类型的，JSON, XML等

##### RPC-网络传输

* TCP/SSL/TLS
* HTTP/HTTPS

##### RPC-原理 查找实现类

通过结构查找服务端的实现类

#### 常见RPC 技术

Hessian:

Thrift

gRPC(cloud native 重用)

#### 如何设计 RPC 框架

##### 选型

* 基于共享接口还是IDL
* 动态代理还是字节码增强
* 序列化使用何种方式
* 网略协议的选型
* 服务店如何查找实现类
* 异常处理(何时，何地)

#### RPC 走向服务化-> 微服务架构

* 多个相同服务如何管理
* 服务的注册发现机制
* 负载均衡，路由等集群功能
* 熔断限流等治理能力
* 重试等策略
* 高可用，监控，性能
