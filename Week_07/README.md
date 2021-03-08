# 学习笔记

## 作业

### 周三

#### ex02 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

* [逐条插入(本机性能较差，故只模拟了10万条数据的情况,耗时约为 587443 毫秒)](db-patch-insert/src/main/java/com/alex/homework/batch/BatchInsert01.java)
* [批处理插入](db-patch-insert/src/main/java/com/alex/homework/batch/BatchInsert02.java)
  * 每一批次 1_0000 数据时，10万条数据下，约为 427507 毫秒
  * 每一批次 10_0000 数据时，10万条数据耗时约为 439890 毫秒
* [使用多条模式，将数据一次性插入，再次之前需要调整packet大小为16M，这次的结果相当惊人，100万数据只用了 5423 毫秒](db-patch-insert/src/main/java/com/alex/homework/batch/BatchInsert03.java)
* [使用prepared statement 及批处理，处理10万数据约为 423103毫秒](db-patch-insert/src/main/java/com/alex/homework/batch/BatchInsert04.java)

### 周日

* [ex02 读写分离 - 动态切换数据源版本 1.0](split-data-source-by-injection/src/main/java/com/alex/homework/splitdatasourcebyinjection/SplitDataSourceByInjectionApplication.java)
* [ex03 读写分离 - 数据库框架版本 2.0 (not totally completed)](split-data-source-by-framework/src/main/java/com/alex/homework/splitdatasourcebyframework/SplitDataSourceByFrameworkApplication.java)

## 笔记

### 事务以及锁

* 事务可靠性模型ACID
  * Atomicity:原子性。要么全部成功，要么全部失败
  * Consistency:一致性。跨表，跨行，跨库，跨事务，数据库始终保持一致状态
  * Isolaltion:隔离性，可见性。保护事务不会互相受到干扰，包含4中隔离级别
  * Durability:持久性，事务提交后，不会丢数据

* 表级锁
  * 上锁前需要先上意向锁
  * 意向锁：表名事务稍后需要进行哪种类型的锁定
    * 共享意向锁(IS)
    * 排他意向锁(IX)
    * Insert意向锁
    * X(排他) 锁与其他锁都冲突
    * I(意向) 锁之间是兼容的
    * S(共享) 锁之间是兼容的
    * S(共享) 与 IX(排他意向) 不兼容
  * 其他锁
    * 自增锁
  * lock tables/undo tables/DDL

* 行级锁
  * 记录锁(Record): 始终记录索引记录，注意隐藏的聚集索引
  * 间隙锁(Gap): 锁住一个范围
  * 临键锁(Next-key): 记录所+间隙锁的组合，可以锁定不存在的数据
  * 谓词锁(Predicate): 空间索引

* 死锁
  * 阻塞与互相等待
  * 增删改锁定读
  * 死锁检测与自动回滚
  * 锁粒度与程序设计
  * 环状死锁的解决思路是使其中一个环节失败或终止
  * 通过超时机制破坏锁，释放资源

* 隔离级别
  * 读未提交: Read UnCommitted
    * 很少使用，不能保证一致性
    * 不使用锁，可能产生脏读，幻读，不可重复读
  * 读已提交: Read Committed
    * 每次查询都会设置和读取自己的新快照
    * 仅支持基于行的 bin log
    * 不加锁的情况下，其他事务的update 或 delete 会对查询结果有影响
    * 加锁后，不锁定间隙，其他事务可以 insert 从而产生幻读
  * 可重复度: Repeatable Read
    * InnoDB 默认隔离级别
    * 使用事务第一次读取时创建的快照
    * 采用多版本控制
    * 使用唯一索引的唯一查询条件时，只锁定查找到的记录，不锁定间隙
    * 其他查询条件时，会锁定扫描到的索引范围，通过间隙锁或临键锁来阻止其他会话在这个范围中插入值
    * InnnDB 不能保证没有幻读，需要加锁
  * 可串行化: Serializable
    * 最严格的级别，事务串行执行，资源消耗最大
  * MySQL 中设置隔离级别
    * 可以设置全局的默认隔离级别
    * 可以单独设置会话的隔离级别
    * InnoDB 基于 MVCC(多版本并发控制)

* undo log
  * 保障事务的原子性
  * 用处：事务回滚，一致性读，崩溃恢复
  * 记录事务回滚时所需要的撤销操作
    * 一条 insert 操作，将对应一条 delete 的 undo log
    * 每个 update 操作，对应一条相反 update 的 undo log
  
  * 保存的位置：
    * system tablespace(5.7 default)
    * undo tablespaces(8.0 default)
* redo log
  * 确保事务的持久性，防止事务提交后数据未刷新到磁盘就掉电崩溃
  * 事务执行过程中写入 redo log, 记录数据对哪些数据页做了哪些修改
  * 提升性能 WAL(Write-Ahead Logging), 先写日志，再刷盘
  * 日志文件
    * ib_logfile0
    * ib_logfile1
  * 日志缓冲: innodb_log_buffer_size
  * 强刷: `fsync()`

* MVCC 多版本并发控制
  * 使InnoDB支持一致性读，读提交以及可重复度
  * 让查询不被阻塞，无需等待被其他事务持有的锁，增加并发性能
  * InnoDB 保留被修改行的旧版本
  * 查询正在被其他事务更新的数据时，会读取更新之前的版本
  * 每行数据都存在一个版本号， 每次更新时都更新该版本
  * 聚集索引的更新=替换更新
  * 二级索引的更新=删除+新建

* MVCC 的实现机制
  * 每一行都隐藏了几个列
    * db_trx_id:6 byte, 最后插入或更新该行的事务的id
    * db_roll_ptr: 7 byte,回滚指针。指向回滚段中写入undo log 的记录  
    * db_row_id: 6 byte，聚集索引 row id
  * 事务链表，保存未提交的事务，事务提交则会从链表中删除
  * read view: 每个sql一个，包括 rw_trx_ids, low_limit_id, up_limit_id, low_limit_no等
  * 回滚段，通过undo log 动态构建的旧版本数据

### SQL 优化

* 查询条件中的隐式转换带来的索引失效问题
* 通过慢查询日志定位问题(slow query)
* 通过监控定位问题
  * 监控3要素
    * 采集间隔
    * 指标计算的方法，最大值，最小值，平均值
    * 数据来源
* 索引的类型
  * Hash
  * B-Tree 每个节点都会存放数据
  * B+ Tree 只在叶子节点存数据（对范围查询友好)
* 主键单调递增避免了页分裂的问题
* 字段选择性-最左原则
  * 某个字段的重复程度，成为该字段的选择性
  * F=distinct(col) / count(*),在[0,1]区间内，越接近1，越适合作为索引

* 修改表结构的代价
  * 索引重建
  * 锁表
  * 抢占资源
  * 主从延时

* 引起索引失效的场景
  * null, not null, not, 函数
  * or 可以用union(去重，union all 不去重)替换, 或者范围确定的，应当使用in
  * 海量数据，如果业务需要的检索条件过多，实际上是无法阻止合适的索引的，这种情况下，应该使用全文检索技术
  * 必要的时候可以通过force index 来要求数据库强制走某个索引

* 主键id的生成思路
  * 自增
  * sequence
  * 模拟seq(预分配id段，会引起id不连续的问题)
  * UUID(128 bit,离散型比较好，但是生成性能较差)
  * 时间戳/随机数
  * snowflake(主流，更适合分布式)

* 高效分页
  * 分页：count/pageSize/pageNum,带条件的查询语句
  * 分页插件(用户自己实现)，查询中嵌套count，有性能问题
  * limit 的数据靠后时，可以使用反序
  * 非精确分页
  * 通过id范围缩小查询范围

### 分库分表

#### 单机数据库的问题

随着数据量增大，尤其是并发的提升，单机MySql将出现:

1. 容量有限，难以扩容
2. 读写压力，QPS查询高，TQPS低(相对来说)。特别是分析类需求会影响到业务，读(Query)远远大于写(Transaction)
3. 可用性不足，容易宕机

单机 MySQL 的技术演进

* 读写压力高 -> 多机集群 -> 主从复制(1主多从) -> 高可用存在可能 -> 故障转移 -> 主从切换
* 容量问题 -> 拆分 -> 分库分表
  * 垂直拆分 -> 按照业务 -> 有瓶颈(业务无法分割)
  * 水平拆分 -> 业务无关

单机演进到多机后，带来了数据一致性的问题

* 跨库跨实例的数据一致性问标如何解决? => 分布式事务
  * XA(刚性事务): 通过客户端的额外操作，或者引入第三方框架，协调各个实例单机事务组成的大事务。常见的有SAGA,TCC, AT
  * 柔性事务

#### 主从复制

* 核心
  * 主库写bin log, 由主库起单独线程，将 bin log 发送给从库
  * 从库起i/o 线程，处理relay log，异步写入自己的bin log
* bin log 的三种模式
  * row(日志较大，记录变化前后的整行数据，非常精确)
  * statement(日志较少，记录 sql 语句)
  * mixed

