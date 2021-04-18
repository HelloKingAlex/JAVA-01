# Kafka 集群 On Windows

## 安装及验证

### 准备工作

1. 操作系统: Win10
2. Kafka 版本: kafka_2.13-2.7.0(windows 下注意安装目录不要有空格, 本文使用的是 `D:\kafka_2.13-2.7.0` )

### 配置(目录起始位置为 kafka 目录的根目录)

+ 监听配置(/config/server.properties)

```bash
listeners=PLAINTEXT://localhost:9092
```

### 启动过程

+ 启动 zookeeper

```bash
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

+ 启动 kafka

```bash
bin\windows\kafka-server-start.bat config\server.properties
```

### 相关命令

```bash
# 列举 topic 
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list

# 创建 3分区 1副本 的 topic:hello-kafka
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --create --topic hello-kafka --partitions 3 --replication-factor 1

# 打印 topic:hello-kafka 的描述信息
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --describe --topic hello-kafka
```

### 发送消息

```bash
# 启动 producer 客户端 broker 为 localhost:9092, topic 为 hello-kafka
bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic hello-kafka

# 发送 2 条消息
>this is the 1st message
>this is the 2nd message
```

### 消费消息

```bash
# 启动 consumer 客户端 broker 为 localhost:9092, topic 为 hello-kafka 消费位置为 从头开始
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --from-beginning --topic hello-kafka
# 启动后，收到了发送到 hello-kafka 的 2条消息
this is the 1st message
this is the 2nd message
```

## 压力测试

### Producer 压测

向 topic:hello-kafka 发送1万条数据，每条数据 1000 个字节,限流 2000 条消息/每秒

```bash
bin\windows\kafka-producer-perf-test.bat --topic hello-kafka --num-records 10000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=localhost:9092
# 测试结果
9974 records sent, 1993.6 records/sec (1.90 MB/sec), 5.5 ms avg latency, 288.0 ms max latency.
10000 records sent, 1991.238550 records/sec (1.90 MB/sec), 5.52 ms avg latency, 288.00 ms max latency, 2 ms 50th, 31 ms 95th, 50 ms 
99th, 54 ms 99.9th.
```

向 topic:hello-kafka 发送1万条数据，每条数据 1000 个字节,限流 20000 条消息/每秒(相当于没有限制)

```bash
bin\windows\kafka-producer-perf-test.bat --topic hello-kafka --num-records 10000 --record-size 1000 --throughput 20000 --producer-props bootstrap.servers=localhost:9092
# 测试结果
10000 records sent, 13003.901170 records/sec (12.40 MB/sec), 146.60 ms avg latency, 291.00 ms max latency, 151 ms 50th, 239 ms 95th, 248 ms 99th, 251 ms 99.9th.
```

不限流的情况下，单条消息的延迟变高了

### Consumer 压测

1 个线程 消费 2 万条消息，每次预取 1024 * 1024 字节数据,超时时间为 10000 毫秒

```bash
bin\windows\kafka-consumer-perf-test.bat --bootstrap-server localhost:9092 --topic hello-kafka --fetch-size 1048576 --messages 20000 --threads 1 --timeout 10000
# 约为 0.4 秒
```

2 个线程 消费 2 万条消息，每次预取 1024 * 1024 字节数据,超时时间为 10000 毫秒

```bash
bin\windows\kafka-consumer-perf-test.bat --bootstrap-server localhost:9092 --topic hello-kafka --fetch-size 1048576 --messages 20000 --threads 2 --timeout 10000
# 约为 0.5 秒
```

小数据量规模无法体现多线程消费的优势，但是依然可以看出 kafka 消费端的性能极强

## 集群环境配置

+ 停止 zookeeper, kafka,清空数据文件（Windows 下，配置指定的目录会在 zookeeper 相关 bat 所在的磁盘的 根目录，我这里是 D:/tmp)
+ 启动 zookeeper
+ 编写集群配置文件(/kafka900[1-3].properties)

```config
# 1,2,3
broker.id=1
num.network.threads=3
socket.send.buffer.bytes=102400
socker.receive.max.bytes=104857600
# node-1,node-2,node-3,目录没有可以创建
log.dirs=D:/kafka-clusters/logs/node-1
num.partitions=1
num.recovery.threads.per.data.dir=1
offsets.topic.replication.factor=1
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1
log.retention.hours=168
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000
zookeeper.connection.timeout.ms=6000000
delete.topic.enable=true
group.initial.rebalance.delay.ms=0
message.max.bytes=5000000
replica.fetch.max.bytes=5000000
# 9001,9002,9003
listeners=PLAINTEXT://localhost:9001
broker.list=localhost:9001,localhost:9002,localhost:9003
zookeeper.connect=localhost:2181

```

+ 分别在三个命令行控制窗口启动 kafka

```bash
bin\windows\kafka-server-start.bat kafka9001.properties
bin\windows\kafka-server-start.bat kafka9002.properties
bin\windows\kafka-server-start.bat kafka9003.properties
```

### 测试集群

创建 3分区 2副本的 topic

```bash
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --create --topic test32 --partitions 3 --replication-factor 2
```

列举 topic 描述信息

```bash
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --describe --topic test32
# 输出如下
Topic: test32   PartitionCount: 3       ReplicationFactor: 2    Configs:        
        Topic: test32   Partition: 0    Leader: 3       Replicas: 3,1   Isr: 3,1
        Topic: test32   Partition: 1    Leader: 1       Replicas: 1,2   Isr: 1,2
        Topic: test32   Partition: 2    Leader: 2       Replicas: 2,3   Isr: 2,3

```

启动 Consumer 客户端

```bash
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9001 --from-beginning --topic test32
```

启动 Producer 客户端

```bash
bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9003 --topic test32
# 发送消息
>this is a test message for kafka-cluster
```