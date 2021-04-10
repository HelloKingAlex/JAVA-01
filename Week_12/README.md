# 学习笔记

## 作业

### Wed

[ex01 配置 redis 的主从复制，sentinel 高可用，Cluster](redis.md)

### Sun

#### ex01 搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费[will be fixed later]

## 笔记

### Redis 高可用

#### Redis-主从复制

```bash
# 由从节点发起跟随主节点
SLAVEOF 主节点ip 主节点端口
# 或者可以在配置文件中直接设置
```

与Mysql 的主从复制一样，主节点负责写，从节点负责(异步复制)

+ 星状主从: 从节点都从主库拉取信息
+ 链式主从：从节点按照链式排布，后续的从节点从前序的从节点拉取数据。缺陷是节点越靠后，与主库的延时越大

从库发起从属于某个主库的时候，从库会丢弃自己与主库差异的部分，保持和主库的一致（避免分布式脑裂问题）

#### 高可用的量化指标

全年系统的可用时间 / 全年总时间 * 100%

N个9的定义：| log(1 -  全年系统的可用时间 / 全年总时间 * 100%) |

例: 99.9% 可用，那么 | log(1-0.999) | = | log(0.001) | = | -3 | = 3

注意区分稳定性和可用性（稳定性看中宕机的频次，越低越好。)

#### Redis Sentinel 主从切换（MHA)

哨兵的职责: 监控主从节点状态，当主机宕机时，将一台从机升级为主机

启动哨兵模式

```bash
redis-sentinel sentinel.conf

# 另一种方式
redis-server redis.conf --sentinel
```

sentinel.conf 主要配置

```config
# 配置需要跟踪的主节点
sentinel monitor mymaster 127.0.0.1 6379 2
# 配置主节点的宕机认定时长
sentinel down-after-milliseconds mymaster 60000
# 配置从节点的故障转移超时时长
sentinel failover-timeout mymaster 180000
# 配置允许并发同步的数量
sentinel paraller-syncs mymaster 1
```

#### Redis 集群-分片（自动分库分表)

Redis Cluster 通过一致性 Hash ，将数据分散到多个节点（16384个slot，16 * 1024 => 16K)。类似的，比如 Java 的 CurrentHashMap。

集群下需要注意的是:

1. 节点间使用 gossip 通信，规模 < 1000
2. 默认所有槽位可用的情况下才提供服务
3. 一般会配合主从模式一起使用

#### Redis 的 Java 分布式组件库-Redission

基于 Netty NIO, API 线程安全

#### 内存网格 Hazelcast

Hazelcast 具有以下特点:

1. 分布式: 数据按照某种策略， 尽可能均匀分布在集群的所有节点上
2. 高可用: 集群的每个节点都是 active 模式，可以提供业务查询和数据修改事务。部分节点不可用时，集群依然可以提供业务服务
3. 可扩展：可以增减服务节点
4. 面向对象: 数据模型都是面向对象和非关系型的
5. 低延迟：基于内存，可以使用堆外内存

Hazelcast 的部署模式：

1. Client-Server模式（ 服务端集群，客户端访问）
2. 嵌入模式（嵌入应用程序内部，本地缓存，本地可以使用堆外内存）

分区：默认分为271个分区，通过 `hazelcast.partition.count` 配置修改

分区副本配置:

```xml
<hazelcast>
    <map name="default">
        <backup-count>0</backup-count>
        <asyync-backup-count>1</async-backup-count>
    </map>
</hazelcast>
```

### 消息队列

#### 系统间的通讯方式

单机的:

1. 基于文件
2. 基于共享内存
3. 基于IPC(管道)

网络的:

1. 基于 Socket
2. 基于数据库
3. 基于 RPC

#### MQ 的优势

1. 异步通信，减少线程等待，提升特大事务，耗时操作对服务器以及对用户的体验
2. 系统解耦
3. 削峰填谷，压力大的时候缓冲部分消息，不至于压垮服务器
4. 可靠通信，提供多种消息模式，服务质量，顺序保障

#### 消息处理模式

1. 点对点(PTP)，对应于 Quere
2. 发布订阅，对应于 Topic

#### 消息处理的保障

1. At most once 至多一次。消息可能丢失，但是不会重复发送
2. At least once 至少一次，消息不会丢失，但是可能会重复
3. Exactly once 精确一次，没调消息肯定会被传输一次且仅一次

消息处理的事务性：

+ 通过确认机制
+ 可以被事务管理器管理 

#### 消息有序性

同一个 Topic 或 Queue 的消息，保障按顺序投递，但是如果做了消息分区，或者批量预取类的操作，有可能会丢失有序性

#### 消息协议

消息协议通常有四层：

1. 网络协议
2. 数据报文
3. 接口API
4. 交互行文

常用消息协议

1. STOMP 简单文本对象消息协议（基于tcp）
2. JMS 客户端协议（仅有API)
3. AMQP 高级MQ协议（完备的消息协议）
4. MQTT 常用于物联网,消息格式比较精简(完备的消息协议)
5. XMPP
6. Open Messaging