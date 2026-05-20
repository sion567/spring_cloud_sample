# 电商系统 - Spring Cloud 微服务

## 项目简介

基于 Spring Cloud 的电商微服务系统，提供用户、商品、订单管理功能。

## 技术栈

- JDK 21
- Spring Boot 3.2.5
- Spring Cloud 2023.0.1
- Spring Data JPA
- Redis
- Kafka
- H2/MySQL

## 项目结构

```
shop-parent/
├── common/
│   ├── common-entity/      # 公共实体
│   ├── common-exception/   # 公共异常
│   └── common-util/        # 公共工具
├── user-service/           # 用户服务 (8081)
├── product-service/        # 商品服务 (8082)
├── order-service/          # 订单服务 (8083)
├── gateway/                # API网关 (8080)
└── monitor-service/        # 监控服务 (8084)
```

## 构建

```bash
# 构建所有模块
./gradlew build

# 构建跳过测试
./gradlew build -x test

# 清理
./gradlew clean
```

## 运行

```bash
# 开发环境 (H2内存数据库)
./gradlew :user-service:bootRun -Pdev
./gradlew :product-service:bootRun -Pdev
./gradlew :order-service:bootRun -Pdev
./gradlew :gateway:bootRun -Pdev
./gradlew :monitor-service:bootRun -Pdev

# 测试环境 (MySQL)
./gradlew :user-service:bootRun -Ptest
./gradlew :product-service:bootRun -Ptest
./gradlew :order-service:bootRun -Ptest
./gradlew :gateway:bootRun -Ptest
./gradlew :monitor-service:bootRun -Ptest
```

## 端口分配

| 服务 | 端口 |
|------|------|
| Gateway | 8080 |
| User Service | 8081 |
| Product Service | 8082 |
| Order Service | 8083 |
| Monitor Service | 8084 |
| H2 Console (dev) | 各服务内嵌 |

## 访问地址

- API网关: http://localhost:8080
- 用户服务: http://localhost:8081
- 商品服务: http://localhost:8082
- 订单服务: http://localhost:8083
- 监控中心: http://localhost:8084
- H2控制台: http://localhost:8081/h2-console (dev模式)
