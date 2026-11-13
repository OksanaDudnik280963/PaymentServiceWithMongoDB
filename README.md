# Spring Boot Microservices with MongoDB

This project contains two Spring Boot services:

- Payment Service: port 8081
- Notification Service: port 8082
- MongoDB: port 27017

## Run with Docker Compose

```bash
docker compose up --build
```

## Payment API

Create payment:

```bash
curl -X POST http://localhost:8081/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-001",
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "amount": 100.00,
    "currency": "GBP",
    "idempotencyKey": "payment-abc-123"
  }'
```

Get payment:

```bash
curl http://localhost:8081/api/payments/{paymentId}
```

## Notification API

```bash
curl -X POST http://localhost:8082/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "event-123",
    "paymentId": "PAY-123",
    "customerId": "CUST-001",
    "customerEmail": "john@example.com",
    "type": "PAYMENT_CREATED",
    "message": "Payment created"
  }'
```

## Health checks

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

## Build locally

```bash
mvn clean install
```

## Build Docker images

```bash
docker compose build
```
## Rebuild Docker images

```powershell
docker compose down
docker rm -f notification-service
docker rm -f payment-service
docker rm -f mongodb
docker compose up --build
```
