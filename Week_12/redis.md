# Redis 高可用

## 主从

+ Docker 方式启动 redis 作为主节点

```bash
docker run --name=redis-master -p 6801:6379 -d redis:6.0.9
```

+ Docker 方式启动 redis 作为从节点

```bash
docker run --name=redis-slave -p 6802:6379 -d redis:6.0.9
```

+ docker 下需要明确知道容器的ip

可以通过`docker inspect --format='{{.NetworkSettings.IPAddress}}' 容器名称|容器id`查询容器的ip

+ docker 进入redis 从机 命令行交互窗口

```bash
# 宿主机 docker 操作
docker exec -it redis-slave bash
# redis 容器打开 redis-cli
redis-cli
```

+ 使当前 redis 节点作为 redis-master 的从机

```bash
SLAVEOF redis-master的ip 端口(这里是6379，也就是redis 的默认端口)
```

+ 验证
  + 在两台节点行都执行 `keys *` ，证明此时二者都处于无数据状态
  + 在主节点，设置 `set abc 123`
  + 在从节点，执行 `get abc` ，输出 "123" 验证完毕

## Sentinel

+ 在主从的基础上，再新建一台从节点 redis-slave2(6803:6379),一台用作哨兵的redis-sentinel(6804:6379),这之后，共有节点4台
  + redis-master
  + redis-slave
  + redis-slave2
  + redis-sentinel

+ 使 redis-slave2 成为 redis-master 的从节点

+ 进入 redis-sentinel 容器内的命令行

```bash
docker exec -it redis-sentinel bash
```

+ 编写哨兵模式配置文件

```bash
cd /
# 我没有在容器里装文本编辑工具，其实可以在建立容器的时候就做好磁盘映射
echo >> sentinel.conf sentinel monitor mymaster redis-master的ip 6379 1
# 启动哨兵模式
redis-sentinel /sentinel.conf
```

+ 验证
  + 强行关闭 redis-master
  + 观察sentinel 命令行窗口，发现切换成功

```bash
# +failover-end master mymaster 172.17.0.2 6379
# +switch-master mymaster 172.17.0.2 6379 172.17.0.3 6379
```

## redis cluster

+ 清空所有 redis 容器，重新建立多个容器
  + redis-01
  + redis-02
  + redis-03
  + redis-04
  + redis-05
  + redis-06

```bash
# 例
docker create --name=redis01 -p 6801:6379 redis:6.0.9 --cluster-enabled yes
```

+ 启动 容器

```bash
# 例
docker start redis01
```

+ 启动集群

```bash
# 在 redis01 的 bash上操作
redis-cli --cluster create \
    172.17.0.2:6379 \
    172.17.0.2:6379 \
    172.17.0.3:6379 \
    172.17.0.4:6379 \
    172.17.0.5:6379 \
    172.17.0.6:6379 \
    --cluster-replicas 1
```

+ 验证

```bash
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 172.17.0.6:6379 to 172.17.0.2:6379
Adding replica 172.17.0.2:6379 to 172.17.0.3:6379
Adding replica 172.17.0.5:6379 to 172.17.0.4:6379
M: 0ee28f0885f523fcdf6f93f0eb752d357ec70adf 172.17.0.2:6379
   slots:[0-5460] (5461 slots) master
S: 0ee28f0885f523fcdf6f93f0eb752d357ec70adf 172.17.0.2:6379
   replicates 3fd3cc9af236025cc075f2a2a3e2db00b130238b
M: 3fd3cc9af236025cc075f2a2a3e2db00b130238b 172.17.0.3:6379
   slots:[5461-10922] (5462 slots) master
M: 09dcd0128cb9c6ed5bc99b5eed1cbce847658e71 172.17.0.4:6379
   slots:[10923-16383] (5461 slots) master
S: eab2f62f4cfccd96bc7468dc3546b287527729f3 172.17.0.5:6379
   replicates 09dcd0128cb9c6ed5bc99b5eed1cbce847658e71
S: 126ad2b63dc22f207149ee5e2347af021be594a5 172.17.0.6:6379
   replicates 0ee28f0885f523fcdf6f93f0eb752d357ec70adf
Can I set the above configuration? (type 'yes' to accept): yes
```
