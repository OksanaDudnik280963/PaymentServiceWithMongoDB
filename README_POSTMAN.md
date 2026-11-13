# Postman Guide — Spring Boot Microservices with MongoDB

This guide explains how to test the Payment Service and Notification Service using Postman.

## Services

| Service | Port | Base URL |
|---|---:|---|
| Payment Service | 8081 | http://localhost:8081 |
| Notification Service | 8082 | http://localhost:8082 |
| MongoDB | 27017 | mongodb://localhost:27017 |

## Start the application

From the project root, run:

```bash
docker compose up --build
```

Check that all containers are running:

```bash
docker ps
```

You should see:

```text
mongodb
payment-service
notification-service
```

## Health checks

### Payment Service

**Method:** `GET`

```text
http://localhost:8081/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

### Notification Service

**Method:** `GET`

```text
http://localhost:8082/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Create a payment

### Request

**Method:** `POST`

```text
http://localhost:8081/api/payments
```

**Headers:**

| Key | Value |
|---|---|
| Content-Type | application/json |

**Body:** select `raw` and `JSON`.

```json
{
  "customerId": "CUST-001",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "amount": 100.00,
  "currency": "GBP",
  "idempotencyKey": "payment-abc-123"
}
```

### Expected response

Status: `201 Created`

```json
{
  "paymentId": "e2b1c9f3-1234-4a56-9cde-7890abcdef12",
  "customerId": "CUST-001",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "amount": 100.00,
  "currency": "GBP",
  "status": "CREATED",
  "idempotencyKey": "payment-abc-123",
  "createdAt": "2026-11-13T15:10:00"
}
```

Copy the value of `paymentId`. You will need it for the GET request.

## Test idempotency

Send the same `POST /api/payments` request again.

Use exactly the same `idempotencyKey`:

```json
{
  "customerId": "CUST-001",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "amount": 100.00,
  "currency": "GBP",
  "idempotencyKey": "payment-abc-123"
}
```

Expected result:

- Status: `201 Created`
- The same `paymentId` is returned
- No duplicate payment is created

This happens because `idempotencyKey` has a unique index in MongoDB.

## Get payment by ID

### Request

**Method:** `GET`

```text
http://localhost:8081/api/payments/{paymentId}
```

Replace `{paymentId}` with the real payment ID from the POST response.

Example:

```text
http://localhost:8081/api/payments/e2b1c9f3-1234-4a56-9cde-7890abcdef12
```

**Headers:**

| Key | Value |
|---|---|
| Accept | application/json |

### Expected response

Status: `200 OK`

```json
{
  "paymentId": "e2b1c9f3-1234-4a56-9cde-7890abcdef12",
  "customerId": "CUST-001",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "amount": 100.00,
  "currency": "GBP",
  "status": "CREATED",
  "idempotencyKey": "payment-abc-123",
  "createdAt": "2026-11-13T15:10:00"
}
```

## Payment not found

### Request

**Method:** `GET`

```text
http://localhost:8081/api/payments/unknown-id
```

### Expected response

Status: `404 Not Found`

```json
{
  "error": "PAYMENT_NOT_FOUND",
  "message": "Payment not found: unknown-id"
}
```

## Invalid payment request

### Request

**Method:** `POST`

```text
http://localhost:8081/api/payments
```

**Body:**

```json
{
  "customerId": "",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "amount": -10,
  "currency": "GBP",
  "idempotencyKey": "invalid-test"
}
```

### Expected response

Status: `400 Bad Request`

```json
{
  "error": "VALIDATION_FAILED",
  "details": {
    "customerId": "must not be blank",
    "amount": "must be greater than 0"
  }
}
```

## Create a notification

### Request

**Method:** `POST`

```text
http://localhost:8082/api/notifications
```

**Headers:**

| Key | Value |
|---|---|
| Content-Type | application/json |

**Body:**

```json
{
  "eventId": "event-123",
  "paymentId": "PAY-123",
  "customerId": "CUST-001",
  "customerEmail": "john@example.com",
  "type": "PAYMENT_CREATED",
  "message": "Payment created successfully"
}
```

### Expected response

Status: `201 Created`

```json
{
  "id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "eventId": "event-123",
  "paymentId": "PAY-123",
  "customerId": "CUST-001",
  "customerEmail": "john@example.com",
  "type": "PAYMENT_CREATED",
  "message": "Payment created successfully",
  "createdAt": "2026-11-13T15:10:00"
}
```

## Notification idempotency

Send the same notification request again with the same `eventId`:

```json
{
  "eventId": "event-123",
  "paymentId": "PAY-123",
  "customerId": "CUST-001",
  "customerEmail": "john@example.com",
  "type": "PAYMENT_CREATED",
  "message": "Payment created successfully"
}
```

Expected result:

- Status: `201 Created`
- The same notification is returned
- No duplicate notification is created

## Postman collection

Create a collection named:

```text
Spring MongoDB Microservices
```

Add these requests:

| Request name | Method | URL |
|---|---|---|
| Payment Health | GET | http://localhost:8081/actuator/health |
| Create Payment | POST | http://localhost:8081/api/payments |
| Get Payment | GET | http://localhost:8081/api/payments/{paymentId} |
| Payment Not Found | GET | http://localhost:8081/api/payments/unknown-id |
| Invalid Payment | POST | http://localhost:8081/api/payments |
| Notification Health | GET | http://localhost:8082/actuator/health |
| Create Notification | POST | http://localhost:8082/api/notifications |

## Check MongoDB data

Open MongoDB shell:

```bash
docker exec -it mongodb mongosh -u admin -p password --authenticationDatabase admin
```

Select the payments database:

```javascript
use payments-db
```

Show collections:

```javascript
show collections
```

Show all payments:

```javascript
db.payments.find().pretty()
```

Select the notifications database:

```javascript
use notifications-db
```

Show all notifications:

```javascript
db.notifications.find().pretty()
```

## MongoDB credentials

| Setting | Value |
|---|---|
| Host | localhost |
| Port | 27017 |
| Username | admin |
| Password | password |
| Authentication database | admin |

Connection string:

```text
mongodb://admin:password@localhost:27017/?authSource=admin
```

## Common problems

| Problem | Solution |
|---|---|
| Connection refused | Make sure Docker containers are running |
| 404 Not Found | Check the payment ID in the GET URL |
| 400 Bad Request | Check required fields and amount value |
| Duplicate key error | Use a new `idempotencyKey` for a new payment |
| MongoDB authentication failed | Use username `admin`, password `password`, and `authSource=admin` |
| Port already in use | Run `docker compose down`, remove old containers, then start again |

## Stop the application

Stop containers but keep MongoDB data:

```bash
docker compose down
```

Stop containers and remove MongoDB data:

```bash
docker compose down -v
```

## Interview explanation

> “I test the Payment Service with Postman. The POST endpoint creates a payment and uses an idempotency key to prevent duplicate payments. The GET endpoint returns a payment by ID. If the same POST request is sent again with the same idempotency key, the service returns the existing payment instead of creating a new one. I also test validation, not-found errors, health endpoints, and MongoDB data.”