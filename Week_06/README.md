学习笔记

# Homework

## week 12

### ex02

1. 用户表 (user)

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

2. 商品 (goods)

```sql
CREATE TABLE `geektime_e_shop`.`goods` (
  `id` INT NOT NULL AUTO_INCREMENT, --商品id
  `vendor_id` INT NOT NULL, --供应商id
  `price` DECIMAL(18,2) NOT NULL, --单价
  `catagory` INT NULL, --分类
  `name` VARCHAR(45) NOT NULL, --商品名称
  PRIMARY KEY (`id`));
```

3. 供应商库存(inventory)

```sql
CREATE TABLE `geektime_e_shop`.`inventory` (
  `id` INT NOT NULL, --库存id
  `goods_id` INT NULL, --商品id
  `vendor_id` INT NULL, --供应商id
  `stock` INT NULL, --库存
  PRIMARY KEY (`id`));
```

4. 供应商信息(vendor)

```sql
CREATE TABLE `geektime_e_shop`.`vendor` (
  `id` INT NOT NULL, --供应商id
  `name` VARCHAR(45) NULL, --供应商名称
  `level` INT NULL, --供应商等级
  PRIMARY KEY (`id`));
```

5. 订单(order)

```sql
CREATE TABLE `geektime_e_shop`.`order` (
  `id` INT NOT NULL, --订单id
  `user_id` INT NULL, --用户id
  `total_price` DECIMAL(18,2) NULL, --总价
  `total_discount` DECIMAL(18,2) NULL, --总折扣
  `status` VARCHAR(1) NULL, --订单状态(未支付，付款中，已成交，作废中，已作废)
  PRIMARY KEY (`id`));
```

6. 订单详情(order_detail)

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

7. 订单状态(order_status_dic)

```sql
CREATE TABLE `geektime_e_shop`.`order_status_dic` (
  `status` VARCHAR(1) NOT NULL, --状态号
  `description` VARCHAR(10) NULL, --描述
  PRIMARY KEY (`status`));
```