# 学习总结

## 作业

### week 12

#### ex02

* 建库及指定字符集和排序规则

```sql
create database geektime_e_shop default character set utfbmb4 collate utf8mb4_unicode_ci
```

* 用户表 (user)

```sql
CREATE TABLE `geektime_e_shop`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT, --用户id
  `name` VARCHAR(20) NOT NULL, --用户名
  `password` VARCHAR(15) NOT NULL, --密码
  `nickname` VARCHAR(20) NOT NULL, --昵称
  `cetification` VARCHAR(20) NOT NULL, --证件号
  `register_time` DATETIME NOT NULL, --注册时间
  PRIMARY KEY (`id`));
```

* 商品 (goods)

```sql
CREATE TABLE `geektime_e_shop`.`goods` (
  `id` INT NOT NULL AUTO_INCREMENT, --商品id
  `vendor_id` INT NOT NULL, --供应商id
  `price` DECIMAL(18,2) NOT NULL, --单价
  `catagory` INT NULL, --分类
  `name` VARCHAR(45) NOT NULL, --商品名称
  PRIMARY KEY (`id`));
```

* 供应商库存(inventory)

```sql
CREATE TABLE `geektime_e_shop`.`inventory` (
  `id` INT NOT NULL, --库存id
  `goods_id` INT NULL, --商品id
  `vendor_id` INT NULL, --供应商id
  `stock` INT NULL, --库存
  PRIMARY KEY (`id`));
```

* 供应商信息(vendor)

```sql
CREATE TABLE `geektime_e_shop`.`vendor` (
  `id` INT NOT NULL, --供应商id
  `name` VARCHAR(45) NULL, --供应商名称
  `level` INT NULL, --供应商等级
  PRIMARY KEY (`id`));
```

* 订单(order)

```sql
CREATE TABLE `geektime_e_shop`.`order` (
  `id` INT NOT NULL, --订单id
  `user_id` INT NULL, --用户id
  `total_price` DECIMAL(18,2) NULL, --总价
  `total_discount` DECIMAL(18,2) NULL, --总折扣
  `status` VARCHAR(1) NULL, --订单状态(未支付，付款中，已成交，作废中，已作废)
  PRIMARY KEY (`id`));
```

* 订单详情(order_detail)

```sql
CREATE TABLE `geektime_e_shop`.`order_detail` (
  `id` INT NOT NULL AUTO_INCREMENT, --订单详情id
  `goods_id` INT NULL, --商品id
  `quantity` INT NULL, --购买数量
  `unit_price` DECIMAL(18,2) NULL, --单价
  `discount` DECIMAL(18,2) NULL, --折扣
  `total_price` DECIMAL(18,2) NULL, --总价
  PRIMARY KEY (`id`));
```

* 订单状态(order_status_dic)

```sql
CREATE TABLE `geektime_e_shop`.`order_status_dic` (
  `status` VARCHAR(1) NOT NULL, --状态号
  `description` VARCHAR(10) NULL, --描述
  PRIMARY KEY (`status`));
```

## 笔记

### 什么是性能

* 吞吐与延迟
  * 延迟低的，一般情况下吞吐可能会高
* 没有量化就没有改进
  * 通过监控与度量
  * 业务问题的最大性能瓶颈一般都在数据库上
* 80/20 原则
  * 引起性能问题的最重要的20%解决之后，整体性能的80%的问题也同时得到了解决
* 过早的性能优化是万恶之源
  * 需要综合评价，结合实际情况(时间，成本)选择合适的优化时机以及手段
* 脱离场景谈性能都是耍流氓

### DB/SQL 优化是业务系统性能优化的核心

* 业务系统的分类
  * 计算密集型(CPU)
  * 数据密集型(I/O)

* 业务处理本身无状态，数据状态最终要保存到数据库
* 一般来说，DB/SQL操作的消耗在一次处理中占比最大
* 业务系统发展的不同阶段，性能瓶颈要点不同
  * 业务形态的动态变化，导致性能薄弱点也是变化的

### 关系型数据库

* 数据库设计范式
  * 第一范式(1NF): 消除重复数据，每一个列都是原子的，不可进一步拆分
  * 第二范式(2NF): 每一行都被主键唯一标识
  * 第三范式(3NF): 消除传递依赖，表中字段之间的依赖变为表对表的依赖
  * BC范式(BCNF): 3NF 上消除主属性对码的依赖
  * 第四范式(4NF): 消除非平凡的多值依赖
  * 第五范式(5NF): 消除一些不合适的连接依赖

* 结构化查询语言
  * 数据查询语言(DQL): select, where, order by, group by, having
  * 数据操作语言(DML): insert, update, delete
  * 事务控制语言(TCL): commit, savepoint, rollback
  * 数据控制语言(DCL): 权限相关。grant, revoke
  * 数据定义语言(DDL): create, alter, drop, create table, drop table, 及增改索引等
  * 指针控制语言(CCL): declare cursor, fetch into等

### MySQL

* 5.7特性
  * 多主（过去只支持一主多备模式)
  * MGB 高可用
  * 分区表
  * json
  * 性能
  * 修复XA等

* MySQL 的文件存储逻辑
  * 按页存储(默认16k)
  * 每页分成许多块(每一行占用一个块),假设一行数据的大小是 1 kb
  * 数据存储通过 B+ 树
    * N层 B+ 树的前N-1层，存的都是索引
    * 假设索引的类型是bigint,占用 8 Byte
    * 指向下一层的指针占用 6 个 Byte
    * 那么前N层的每一个节点占用的数据是 14 Byte
    * 一个页可以存储的索引数据是 16 * 1024 / 14 = 1170个，也就可以指向1170个页
    * 一个2层的 B+ 树结构，可以指向的总数据条目是 1170(页) * 16(行/页) = 18720(行)
    * 一个3层的 B+ 树结构，可以指向的总数据条目是:第一层可以指向索引页 乘以 第二层索引页可以指向的数据页 乘以 第三层每一页可以存储的数据行 =

      $1170*1170*16 = 21902400(行)$

    * N层 B+ 树，在每一行占用1kb,主键占用8 Byte的情况下，可以存储:

      $1170^{N-1}*16$ 行数据
