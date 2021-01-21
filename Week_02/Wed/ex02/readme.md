# 压测gateway-server-0.0.1-SNAPSHOT.jar
## 准备工作

使用 Super BenchMarker 压测

```bash
sb -u http://localhost:8088/api/hello -c 40 -N 30
```

使用并行GC，在堆内存分配1g的情况下，通过日志文件记录GC情况

```bash
java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/parallelGc.log -XX:+UseParallelGC -jar ./resource/gateway-server-0.0.1-SNAPSHOT.jar
```

![并行GC压测结果](./resource/images/2021-01-21-21-50-54.png)

使用串行GC，在堆内存分配1g的情况下，通过日志文件记录GC情况

```bash
java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/serialGc.log -XX:+UseSerialGC -jar ./resource/gateway-server-0.0.1-SNAPSHOT.jar
```

![串行GC压测结果](./resource/images/2021-01-21-21-52-26.png)

使用CMS GC(并发)，在堆内存分配1g的情况下，通过日志文件记录GC情况

```bash
java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/cmsGc.log -XX:+UseConcMarkSweepGC -jar ./resource/gateway-server-0.0.1-SNAPSHOT.jar
```

![CMS GC压测结果](./resource/images/2021-01-21-21-55-54.png)

使用G1 GC，在堆内存分配1g的情况下，通过日志文件记录GC情况

```bash
java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/g1Gc.log -XX:+UseG1GC -jar ./resource/gateway-server-0.0.1-SNAPSHOT.jar
```

![G1 GC压测结果](./resource/images/2021-01-21-21-57-26.png)