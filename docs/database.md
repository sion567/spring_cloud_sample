# 数据库设计文档

## 1. 数据库概览

| 数据库名 | 服务 | 说明 |
|---------|------|------|
| shop_user | user-service | 用户数据 |
| shop_product | product-service | 商品数据 |
| shop_order | order-service | 订单数据 |

---

## 2. 用户服务数据库 (shop_user)

### 2.1 用户表
```sql
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    level INT DEFAULT 1 COMMENT '会员等级',
    points INT DEFAULT 0 COMMENT '积分',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 2.2 收货地址表
```sql
CREATE TABLE t_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    district VARCHAR(50) NOT NULL COMMENT '区县',
    detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';
```

---

## 3. 商品服务数据库 (shop_product)

### 3.1 商品表
```sql
CREATE TABLE t_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    category_id BIGINT COMMENT '分类ID',
    image_url VARCHAR(500) COMMENT '图片URL',
    status TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
```

### 3.2 商品分类表
```sql
CREATE TABLE t_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';
```

---

## 4. 订单服务数据库 (shop_order)

### 4.1 订单表
```sql
CREATE TABLE t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    address_id BIGINT NOT NULL COMMENT '地址ID',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '订单总金额',
    discount_amount DECIMAL(12,2) DEFAULT 0 COMMENT '优惠金额',
    pay_amount DECIMAL(12,2) NOT NULL COMMENT '实付金额',
    status TINYINT DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消',
    pay_method VARCHAR(20) COMMENT '支付方式',
    pay_no VARCHAR(64) COMMENT '支付流水号',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(255) COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

### 4.2 订单明细表
```sql
CREATE TABLE t_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(12,2) NOT NULL COMMENT '小计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';
```

---

## 5. 分析服务数据库 (shop_analytics)

### 5.1 每日统计表
```sql
CREATE TABLE t_analytics_daily (
    stat_date DATE PRIMARY KEY COMMENT '统计日期',
    new_users INT DEFAULT 0 COMMENT '新增用户',
    active_users INT DEFAULT 0 COMMENT '活跃用户',
    orders INT DEFAULT 0 COMMENT '订单数',
    total_amount DECIMAL(14,2) DEFAULT 0 COMMENT '总金额',
    avg_order_amount DECIMAL(10,2) DEFAULT 0 COMMENT '平均客单价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日统计表';
```

### 5.2 商品销量表
```sql
CREATE TABLE t_product_sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    sales_count INT DEFAULT 0 COMMENT '销量',
    sales_amount DECIMAL(14,2) DEFAULT 0 COMMENT '销售额',
    stat_date DATE NOT NULL COMMENT '统计日期',
    INDEX idx_product_id (product_id),
    INDEX idx_stat_date (stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品销量表';
```

---

## 6. 索引优化建议

### 6.1 查询优化
- 订单查询：`(user_id, status, create_time)`
- 商品搜索：`(category_id, status)`
- 分页查询：使用游标分页避免深度分页

### 6.2 读写分离
- 主库：写操作
- 从库：读操作
- 延迟复制：用于分析统计

---

## 7. 数据初始化脚本

```sql
-- 初始化分类
INSERT INTO t_category (name, parent_id, sort) VALUES
('手机', 0, 1),
('电脑', 0, 2),
('服装', 0, 3);

-- 初始化管理员用户
INSERT INTO t_user (username, password, email, phone, nickname, level, points)
VALUES ('admin', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'admin@shop.com', '13800138000', '管理员', 5, 0);
```
