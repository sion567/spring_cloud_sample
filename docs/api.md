# API 接口文档

## 1. 用户服务 API

### 1.1 用户注册
```
POST /api/user/register
Content-Type: application/json

Request:
{
    "username": "zhangsan",
    "password": "123456",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "nickname": "张三"
}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "zhangsan",
        "nickname": "张三",
        "level": 1,
        "points": 100
    }
}
```

### 1.2 用户登录
```
POST /api/user/login
Content-Type: application/json

Request:
{
    "username": "zhangsan",
    "password": "123456"
}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "user": {
            "id": 1,
            "username": "zhangsan",
            "nickname": "张三"
        }
    }
}
```

### 1.3 获取用户信息
```
GET /api/user/{id}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "username": "zhangsan",
        "email": "zhangsan@example.com",
        "phone": "13800138000",
        "nickname": "张三",
        "level": 2,
        "points": 500,
        "createTime": "2024-01-15 10:30:00"
    }
}
```

### 1.4 添加收货地址
```
POST /api/user/{userId}/address
Content-Type: application/json

Request:
{
    "receiverName": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "detailAddress": "科技园路1号",
    "isDefault": true
}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "receiverName": "张三",
        "phone": "13800138000",
        "fullAddress": "广东省深圳市南山区科技园路1号"
    }
}
```

---

## 2. 商品服务 API

### 2.1 商品列表
```
GET /api/product?page=1&size=10&categoryId=1

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1,
                "name": "iPhone 15 Pro",
                "price": 8999.00,
                "stock": 100,
                "imageUrl": "https://example.com/iphone15.jpg",
                "status": 1
            }
        ],
        "total": 50,
        "page": 1,
        "size": 10
    }
}
```

### 2.2 商品详情
```
GET /api/product/{id}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "name": "iPhone 15 Pro",
        "description": "苹果最新款手机",
        "price": 8999.00,
        "stock": 100,
        "categoryId": 1,
        "categoryName": "手机",
        "imageUrl": "https://example.com/iphone15.jpg",
        "status": 1
    }
}
```

### 2.3 创建商品
```
POST /api/product
Content-Type: application/json

Request:
{
    "name": "iPhone 15 Pro",
    "description": "苹果最新款手机",
    "price": 8999.00,
    "stock": 100,
    "categoryId": 1,
    "imageUrl": "https://example.com/iphone15.jpg"
}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "name": "iPhone 15 Pro"
    }
}
```

### 2.4 更新库存
```
PUT /api/product/{id}/stock
Content-Type: application/json

Request:
{
    "quantity": -1,
    "reason": "order"
}

Response:
{
    "code": 200,
    "message": "success"
}
```

---

## 3. 订单服务 API

### 3.1 创建订单
```
POST /api/order
Content-Type: application/json

Request:
{
    "userId": 1,
    "addressId": 1,
    "items": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 2,
            "quantity": 1
        }
    ]
}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "orderNo": "ORD20240115103000001",
        "totalAmount": 13998.00,
        "status": 0,
        "items": [
            {
                "productId": 1,
                "productName": "iPhone 15 Pro",
                "price": 8999.00,
                "quantity": 2,
                "subtotal": 17998.00
            }
        ]
    }
}
```

### 3.2 订单详情
```
GET /api/order/{id}

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "orderNo": "ORD20240115103000001",
        "userId": 1,
        "totalAmount": 13998.00,
        "status": 1,
        "payTime": "2024-01-15 10:35:00",
        "createTime": "2024-01-15 10:30:00",
        "address": {
            "receiverName": "张三",
            "phone": "13800138000",
            "fullAddress": "广东省深圳市南山区科技园路1号"
        },
        "items": [
            {
                "id": 1,
                "productId": 1,
                "productName": "iPhone 15 Pro",
                "price": 8999.00,
                "quantity": 2,
                "subtotal": 17998.00
            }
        ]
    }
}
```

### 3.3 用户订单列表
```
GET /api/order/user/{userId}?page=1&size=10&status=1

Response:
{
    "code": 200,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1,
                "orderNo": "ORD20240115103000001",
                "totalAmount": 13998.00,
                "status": 1,
                "createTime": "2024-01-15 10:30:00"
            }
        ],
        "total": 5,
        "page": 1,
        "size": 10
    }
}
```

### 3.4 订单支付
```
PUT /api/order/{id}/pay
Content-Type: application/json

Request:
{
    "payMethod": "alipay",
    "payNo": "ALIPAY20240115103500001"
}

Response:
{
    "code": 200,
    "message": "success"
}
```

### 3.5 订单取消
```
PUT /api/order/{id}/cancel

Response:
{
    "code": 200,
    "message": "success"
}
```

---

## 4. 监控服务 API

### 4.1 监控指标
```
GET /actuator/prometheus

Response:
# HELP jvm_gc_memory_allocated_bytes
# TYPE jvm_gc_memory_allocated_bytes gauge
jvm_gc_memory_allocated_bytes{application="user-service",} 2.4358848E8
```

### 4.2 健康检查
```
GET /actuator/health

Response:
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP",
            "details": {
                "database": "MySQL"
            }
        },
        "redis": {
            "status": "UP"
        },
        "kafka": {
            "status": "UP"
        }
    }
}
```

---

## 5. 错误码定义

| 错误码 | 说明 |
|-------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户名已存在 |
| 1002 | 用户名或密码错误 |
| 2001 | 商品不存在 |
| 2002 | 库存不足 |
| 3001 | 订单不存在 |
| 3002 | 订单状态不允许此操作 |
