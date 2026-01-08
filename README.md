# Order Processor Service
## Overview

**Order Processor** is a Spring Boot–based backend service that processes orders using a file-driven and event-driven architecture, secured with JWT-based authentication and role-based authorization.

## Tech Stack

- Java 21

- Spring Boot 3.x

- Spring Security (JWT)

- Apache Camel

- RabbitMQ

- JPA + H2

- Maven

## Setup & Run Instructions

### Prerequisites

- Java 21

- Maven

### RabbitMQ Management UI:
http://localhost:15672
(Default credentials: guest / guest)

### Build the Application

```declarative
mvn clean install
```

### Run the Application

```declarative
mvn spring-boot:run
```


**APPLICATION URL** : http://localhost:8080

## API Documentation

### Login API (Public)

```declarative
POST /auth/login
```

**REQUEST**
```declarative
{
  "username": "<username>",
  "password": "<password>"
}
```
**RESPONSE**
```declarative
{
"token": "<token>"
}
```

The JWT token must be sent with every secured API request
```declarative
Authorization: Bearer <JWT_TOKEN>
```



### Create Order (ADMIN / USER)

```declarative
POST /api/orders
```

**REQUEST**
```declarative
{
"customerId" : "CUST201",
"product": "Sugar",
"price":20
}
```
**RESPONSE**
```declarative
{
"orderId": "8a23a6cf-1820-4175-8373-054a1e9fa78a",
"status": "CREATED"
}
```


### View All Orders (ADMIN Only)

```declarative
GET /api/orders/all
```

**RESPONSE** List<Order>
```declarative
[
{
"orderId": "d7573337-da55-429b-8e8e-14c75b0d85c1",
"customerId": "CUST101",
"product": "Salt",
"price": 20.0,
"status": "CREATED"
},....
]
```



### View Own Orders (USER Only)

```declarative
GET /api/orders/my
```

**RESPONSE** List<Order>
```declarative
[
{
"orderId": "d7573337-da55-429b-8e8e-14c75b0d85c1",
"customerId": "CUST101",
"product": "Salt",
"price": 20.0,
"status": "CREATED"
},....
]
```


### View Own Orders -OrderId

```declarative
GET /api/orders/<orderId>
```

**RESPONSE**
```declarative
{
"orderId": "d7573337-da55-429b-8e8e-14c75b0d85c1",
"customerId": "CUST101",
"product": "Salt",
"price": 20.0,
"status": "CREATED"
}
```


### View Own Orders -CustomerID

```declarative
GET /api/orders
```

**REQUEST**
```declarative
{
"customerId" : "CUST201",
}
```

**RESPONSE** List<Order>
```declarative
[{
"orderId": "d7573337-da55-429b-8e8e-14c75b0d85c1",
"customerId": "CUST101",
"product": "Salt",
"price": 20.0,
"status": "CREATED"
},....
]
```


## Apache Camel – File Routing

Automatically processes order files dropped into a directory(/input/orders) and publish them as events to RabbitMQ.

```
File System → Apache Camel → JSON Unmarshal → RabbitMQ Queue
```

- Input Directory: input/orders

- Processed Directory: processed/orders

- File Format: JSON

- Output Queue: ORDER.CREATED.QUEUE

### Working Explained

- Apache Camel detects a new file

- File is unmarshalled into an Order DTO

- Order is logged and validated

- Message is published to RabbitMQ

- File is moved to processed/orders

## RabbitMQ Message Consumption & Logging

Consumes order events from RabbitMQ and log them for monitoring

```declarative
RabbitMQ Queue → Message Consumer → Logging
```

### Working Explained
- Listens to the queue asynchronously

- Receives order messages

- Logs order details

- Acknowledges messages



