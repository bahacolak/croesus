# 🚀 Croesus API Testing Guide

## �� Postman Collection Usage Guide

You can test all endpoints of the Croesus microservice infrastructure with this collection.

### 🔧 Setup

1. **Import Collection in Postman:**
   - Open Postman
   - Click the `Import` button
   - Select the `Croesus_API_Collection.json` file
   - Collection will be loaded automatically

2. **Environment Variables:**
   The collection automatically uses these variables:
   ```
   base_url: http://localhost
   user_service_port: 8081
   portfolio_service_port: 8082
   wallet_service_port: 8083
   trading_service_port: 8084
   market_service_port: 8085
   gateway_port: 8080
   jwt_token: (automatically set)
   user_id: (automatically set)
   ```

### 🎯 Test Scenario

#### 1. 🔐 **Authentication (Start Here)**
```
1. Register User → Create new user
2. Login User → Get JWT token (automatically saved)
```

#### 2. 👤 **User Management**
```
3. Get Current User → Logged-in user information
4. Get User by ID → Specific user information
5. Get User Wallet Balance → User wallet balance
```

#### 3. 💰 **Wallet Operations**
```
6. Get Current User Wallet → Wallet details
7. Deposit Money → Deposit money (e.g., 1000 USD)
8. Get Current User Balance → Current balance
9. Withdraw Money → Withdraw money
10. Transfer Money → Transfer to another user
11. Get Wallet Transactions → Transaction history
```

#### 4. 📈 **Market Data**
```
12. Get All Cryptocurrencies → All crypto list
13. Get Crypto by Symbol → Bitcoin details
14. Get Crypto Price → Bitcoin price
15. Search Cryptocurrencies → Crypto search
16. Get Top Gainers → Top performers
17. Get Market Overview → Market overview
```

#### 5. 🔄 **Trading Operations**
```
18. Buy Asset → Buy Bitcoin (0.001 BTC)
19. Get User Transactions → Transaction history
20. Get Transaction Summary → Transaction summary
21. Sell Asset → Sell Bitcoin
```

#### 6. 💼 **Portfolio Management**
```
22. Get User Portfolio → View portfolio
23. Get Portfolio Summary → Portfolio summary
24. Get All Assets → Asset list
```

### 📊 **Test Data Examples**

#### Register Request:
```json
{
  "username": "testuser",
  "email": "test@example.com", 
  "password": "password123",
  "fullName": "Test User",
  "roles": ["user"]
}
```

#### Login Request:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

#### Deposit Request:
```json
{
  "amount": 1000.00,
  "description": "Initial deposit",
  "referenceId": "DEP001"
}
```

#### Buy Asset Request:
```json
{
  "symbol": "bitcoin",
  "quantity": 0.001,
  "description": "Buy some Bitcoin"
}
```

#### Transfer Request:
```json
{
  "targetUserId": 2,
  "amount": 50.00,
  "description": "Transfer to friend"
}
```

### 🎛️ **Service Ports**

| Service | Port | Direct Access |
|---------|------|---------------|
| API Gateway | 8080 | http://localhost:8080 |
| User Service | 8081 | http://localhost:8081 |
| Portfolio Service | 8082 | http://localhost:8082 |
| Wallet Service | 8083 | http://localhost:8083 |
| Trading Service | 8084 | http://localhost:8084 |
| Market Service | 8085 | http://localhost:8085 |
| Eureka Server | 8761 | http://localhost:8761 |

### 🔍 **Test Validation**

The collection includes automatic test scripts:

- **Login Test:** Automatically saves JWT token
- **Response Status:** Checks HTTP status codes
- **Authorization:** Automatically adds Bearer token

### 🌐 **API Gateway vs Direct Access**

1. **Direct Service Access:** 
   - Direct access to each service via its port
   - Ideal for development and debugging

2. **API Gateway Access:**
   - Single entry point (port 8080)
   - Production-like routing
   - Load balancing and security

### 🚨 **Troubleshooting**

#### Common Errors:
- **401 Unauthorized:** Missing/invalid JWT token
- **404 Not Found:** Wrong endpoint/service down
- **400 Bad Request:** Invalid request body
- **500 Internal Error:** Service error

#### Debug Steps:
1. Check if services are running: `docker ps`
2. Test health check endpoints
3. Verify JWT token is valid
4. Validate request body format

### 📝 **Notes**

- Always start with Register + Login first
- You cannot trade without money in wallet
- Transactions may be asynchronous
- Health check endpoints don't require authentication
- Admin endpoints require ADMIN role

### 🎉 **Successful Test Flow**

1. ✅ Register → Login → JWT obtained
2. ✅ Deposit → 1000 USD deposited  
3. ✅ Market Data → Bitcoin price fetched
4. ✅ Buy Asset → 0.001 BTC purchased
5. ✅ Portfolio → Bitcoin displayed
6. ✅ Sell Asset → 0.0005 BTC sold
7. ✅ Transactions → Operations listed

You can test the entire microservice infrastructure and accelerate your development process with this collection! 🚀 