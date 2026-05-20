# 电商系统技术开发文档

## 1. 项目概述

### 1.1 项目简介
基于 Spring Cloud 微服务架构的电商系统，提供用户、商品、订单管理功能，集成 Redis 缓存、Kafka 消息队列、监控与分析功能。

### 1.2 技术栈
| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | LTS 版本 |
| Spring Boot | 3.2.x | 基础框架 |
| Spring Cloud | 2023.x | 微服务框架 |
| Spring Data JPA | 3.2.x | 数据访问 |
| Redis | 7.x | 缓存与会话 |
| Kafka | 3.x | 消息队列 |
| MySQL | 8.x | 主数据库 |

### 1.3 微服务架构

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
│                     (Spring Cloud Gateway)                   │
└─────────────────────────────────────────────────────────────┘
         │                │                │                │
    ┌────▼────┐      ┌────▼────┐      ┌────▼────┐      ┌────▼────┐
    │ User    │      │ Product │      │ Order   │      │ Monitor │
    │ Service │      │ Service │      │ Service │      │ Service │
    └────┬────┘      └────┬────┘      └────┬────┘      └────┬────┘
         │                │                │                │
    ┌────▼────┐      ┌────▼────┐      ┌────▼────┐      ┌────▼────┐
    │  MySQL  │      │  MySQL  │      │  MySQL  │      │ Redis   │
    │ (User)  │      │(Product)│      │ (Order) │      │(Metrics)│
    └─────────┘      └─────────┘      └─────────┘      └─────────┘
```

---

## 2. 模块设计

### 2.1 用户服务 (user-service)

#### 2.1.1 核心功能
- 用户注册与登录
- 用户信息管理
- 收货地址管理
- 会员等级管理

#### 2.1.2 数据模型

```java
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private Integer level;        // 会员等级
    private Integer points;      // 积分
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

@Entity
@Table(name = "t_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Boolean isDefault;
}
```

#### 2.1.3 API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/register | 用户注册 |
| POST | /api/user/login | 用户登录 |
| GET | /api/user/{id} | 获取用户信息 |
| PUT | /api/user/{id} | 更新用户信息 |
| GET | /api/user/{id}/address | 获取收货地址 |
| POST | /api/user/{id}/address | 添加收货地址 |

---

### 2.2 商品服务 (product-service)

#### 2.2.1 核心功能
- 商品 CRUD
- 商品分类管理
- 库存管理
- 商品搜索

#### 2.2.2 数据模型

```java
@Entity
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String imageUrl;
    private Integer status;       // 0-下架 1-上架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

@Entity
@Table(name = "t_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
}
```

#### 2.2.3 API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/product | 商品列表(分页) |
| GET | /api/product/{id} | 商品详情 |
| POST | /api/product | 创建商品 |
| PUT | /api/product/{id} | 更新商品 |
| DELETE | /api/product/{id} | 删除商品 |
| PUT | /api/product/{id}/stock | 库存更新 |
| GET | /api/category | 分类列表 |

---

### 2.3 订单服务 (order-service)

#### 2.3.1 核心功能
- 订单创建
- 订单支付
- 订单取消
- 订单查询
- 订单状态流转

#### 2.3.2 数据模型

```java
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;           // 订单号
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private Integer status;           // 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}

@Entity
@Table(name = "t_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
```

#### 2.3.3 API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/order | 创建订单 |
| GET | /api/order/{id} | 订单详情 |
| GET | /api/order/no/{orderNo} | 按订单号查询 |
| GET | /api/order/user/{userId} | 用户订单列表 |
| PUT | /api/order/{id}/pay | 订单支付 |
| PUT | /api/order/{id}/cancel | 订单取消 |
| PUT | /api/order/{id}/ship | 订单发货 |

---

## 3. 公共服务模块

### 3.1 公共实体 (common-entity)
```java
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
}
```

### 3.2 公共异常
```java
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
```

---

## 4. Redis 缓存设计

### 4.1 缓存策略

| 数据类型 | Key 模式 | 过期时间 | 说明 |
|---------|---------|---------|------|
| 用户信息 | user:{id} | 30分钟 | 用户数据缓存 |
| 商品信息 | product:{id} | 15分钟 | 商品数据缓存 |
| 商品列表 | product:list:{page} | 5分钟 | 商品列表缓存 |
| 热点商品 | product:hot:{id} | 1小时 | 热销商品缓存 |
| 订单信息 | order:{id} | 20分钟 | 订单缓存 |

### 4.2 分布式会话
```yaml
spring:
  session:
    store-type: redis
    redis:
      namespace: shop:session
```

---

## 5. Kafka 消息设计

### 5.1 Topic 定义

| Topic 名称 | 说明 | 分区数 |
|-----------|------|--------|
| shop-order-create | 订单创建事件 | 3 |
| shop-order-pay | 订单支付事件 | 3 |
| shop-product-stock | 库存变更事件 | 5 |
| shop-user-points | 积分变更事件 | 2 |
| shop-analytics | 分析数据事件 | 3 |

### 5.2 消息格式

```java
public class OrderEvent {
    private String eventId;
    private String eventType;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
```

### 5.3 消费者组

| 消费者组 | 订阅 Topic | 说明 |
|---------|-----------|------|
| order-consumer-group | shop-order-create, shop-order-pay | 订单服务消费者 |
| stock-consumer-group | shop-product-stock | 库存服务消费者 |
| analytics-consumer-group | shop-analytics | 分析服务消费者 |

---

## 6. 监控与运维

### 6.1 Spring Boot Admin 监控

```yaml
spring-boot-admin:
  client:
    url: http://localhost:8080
    instance:
      service-url: http://localhost:${server.port}
```

### 6.2 Micrometer + Prometheus 指标

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    tags:
      application: ${spring.application.name}
```

### 6.3 核心监控指标

| 指标名称 | 类型 | 说明 |
|---------|------|------|
| jvm_gc_memory_allocated | Gauge | GC 内存分配 |
| jvm_gc_pause | Timer | GC 暂停时间 |
| http_server_requests | Timer | HTTP 请求耗时 |
| kafka_producer_record_send | Timer | Kafka 发送延迟 |
| redis_connection_pool | Gauge | Redis 连接池 |
| hikaricp_connections | Gauge | 数据库连接池 |

### 6.4 链路追踪

```yaml
spring:
  cloud:
    sleuth:
      sampler:
        probability: 1.0
    zipkin:
      base-url: http://localhost:9411
```

---

## 7. 分析模块

### 7.1 数据分析功能

#### 7.1.1 交易分析
- 每日/每周/每月订单量
- 客单价分析
- 订单转化率

#### 7.1.2 用户分析
- 新增用户数
- 活跃用户数
- 用户留存率

#### 7.1.3 商品分析
- 商品销量排行
- 商品浏览量
- 商品收藏数

### 7.2 统计数据模型

```java
@Entity
@Table(name = "t_analytics_daily")
public class AnalyticsDaily {
    @Id
    private LocalDate statDate;
    private Integer newUsers;
    private Integer activeUsers;
    private Integer orders;
    private BigDecimal totalAmount;
    private BigDecimal avgOrderAmount;
}
```

### 7.3 Kafka 消费分析

```java
@KafkaListener(topics = "shop-analytics", groupId = "analytics-consumer-group")
public void consumeAnalyticsEvent(OrderEvent event) {
    analyticsService.processEvent(event);
}
```

---

## 8. 项目结构

```
shop-parent/
├── pom.xml
├── common/
│   ├── common-entity/
│   ├── common-exception/
│   └── common-util/
├── user-service/
│   ├── src/main/java/
│   │   └── com/shop/user/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       └── config/
│   └── src/main/resources/
│       └── application.yml
├── product-service/
│   └── ...
├── order-service/
│   └── ...
├── monitor-service/
│   └── ...
├── gateway/
│   └── ...
└── docs/
    └── api.md
```

---

## 9. 核心配置

### 9.1 application.yml (示例)

```yaml
spring:
  application:
    name: user-service
  datasource:
    url: jdbc:mysql://localhost:3306/shop_user?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  data:
    redis:
      host: localhost
      port: 6379
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: user-service-group
      auto-offset-reset: earliest

server:
  port: 8081

spring-boot-admin:
  client:
    url: http://localhost:8080
```

---

## 10. API 网关配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/product/**
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/order/**
```

---

## 11. 开发规范

### 11.1 命名规范
- 类名：大写驼峰 `UserService`
- 方法名：小写驼峰 `getUserById`
- 包名：小写 `com.shop.user`
- 常量：全大写下划线 `MAX_RETRY_COUNT`

### 11.2 分层规范
```
Controller -> Service -> Repository
    │           │           │
   接收请求    业务逻辑    数据访问
```

### 11.3 异常处理
- 统一异常拦截 `@ControllerAdvice`
- 业务异常 `BusinessException`
- 参数校验 `@Valid`

---

## 12. 部署架构

```
                    ┌─────────────┐
                    │   Nginx     │
                    │  (反向代理)  │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
        ┌─────▼─────┐ ┌────▼─────┐ ┌────▼─────┐
        │ Gateway   │ │ Admin    │ │ Zipkin   │
        │ :8080     │ │ :8080    │ │ :9411    │
        └─────┬─────┘ └──────────┘ └──────────┘
              │
    ┌─────────┼─────────┼─────────┐
    │         │         │         │
┌───▼───┐ ┌───▼───┐ ┌───▼───┐ ┌───▼───┐
│ User  │ │Product│ │ Order │ │Monitor│
│ :8081 │ │ :8082 │ │ :8083 │ │ :8084 │
└───┬───┘ └───┬───┘ └───┬───┘ └───────┘
    │         │         │
┌───┴───┐ ┌───┴───┐ ┌───┴───┐
│ MySQL │ │ MySQL │ │ MySQL │
│ Redis │ │ Redis │ │ Redis │
│ Kafka │ │ Kafka │ │ Kafka │
└───────┘ └───────┘ └───────┘
```

---

## 13. 待办事项

- [ ] 创建 Maven 父项目
- [ ] 创建公共模块
- [ ] 实现用户服务
- [ ] 实现商品服务
- [ ] 实现订单服务
- [ ] 配置 API 网关
- [ ] 配置监控中心
- [ ] 集成 Kafka 消息队列
- [ ] 编写单元测试
