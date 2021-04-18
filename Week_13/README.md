# 学习笔记

## 作业

### Wed

#### ex01 搭建一个 3 节点 Kafka 集群，测试功能和性能；实现 spring kafka 下对 kafka 集群的操作

+ [集群环境搭建](kafka.md)
+ [spring kafka 操作 kafka集群](kafka-clusters-demo/src/main/java/com/alex/homework/demo/KafkaClusterDemoApplication.java)

## 笔记

### 分布式消息-Kafka 消息中间件

kafka作为分布式的消息订阅系统，其设计目标是:

1. 已O(1)的时间复杂度，支持TB级一下数据的访问
2. 单机100K条/s 数据吞吐量
3. 支持服务端的分区，分布式消费，同时保障不同Partition内的消息的顺序传输
4. 支持离线数据和实时数据处理
5. 支持在线水平扩展

#### Kafka 的基本角色

1. Broker: 服务集群(一个或多台服务器)
2. Topic：消息的类别。物理上分开，逻辑上不分开
3. Partition: 物理上的概念，一个Topic 包含 >= 1 个 Partition
4. Producer: 负责发布消息到 Kafka Broker
5. Consumer: 消费者，向 Broker 读取消息的客户端
6. Consumer Group：每个 Consumer 属于一个 Group，不指定的情况下有默认的 group 归属

#### Topic 特性

1. 通过 Partition 增加可扩展性
2. 通过顺序写入达到高吞吐(只追加写，不修改和删除)
3. 多副本增加容错(Leader, Follower)
