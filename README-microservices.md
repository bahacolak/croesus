# 🏗️ Croesus Microservices Architecture

Croesus digital asset management platform microservices migration project.

## 🚀 Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │  Eureka Server  │
│   (React)       │◄──►│   (Port: 8080)  │◄──►│  (Port: 8761)   │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Config Server  │    │   RabbitMQ      │    │   PostgreSQL    │
│  (Port: 8888)   │    │  (Port: 5672)   │    │   Databases     │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                ▼               ▼               ▼
    ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
    │  User Service   │ │Portfolio Service│ │Trading Service  │
    │                 │ │                 │ │                 │
    └─────────────────┘ └─────────────────┘ └─────────────────┘
                                │
                                ▼
                    ┌─────────────────┐
                    │ Market Service  │
                    │                 │
                    └─────────────────┘
```

## 📁 Project Structure

```
croesus/
├── eureka-server/              # Service Discovery
├── api-gateway/                # API Gateway
├── config-server/              # Configuration Server
├── user-service/               # User management
├── portfolio-service/          # Portfolio management
├── trading-service/            # Trading operations
├── market-service/             # Market data
├── shared/                     # Shared libraries
│   └── common/                 # Common utilities
├── monolith/                   # Legacy monolithic code (backup)
├── frontend/                   # React frontend
├── docker-compose-microservices.yml
├── env-microservices.example
└── README-microservices.md
```

## 🛠️ Technology Stack

### Infrastructure
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Configuration**: Spring Cloud Config
- **Message Broker**: RabbitMQ
- **Databases**: PostgreSQL (separate for each service)

### Microservices
- **Framework**: Spring Boot 3.4.5
- **Cloud**: Spring Cloud 2023.0.5
- **Security**: JWT Authentication
- **Database**: Spring Data JPA + PostgreSQL
- **Communication**: REST + RabbitMQ

## 🚀 Quick Start

### 1. Prepare Environment File
```bash
cp env-microservices.example .env
# Edit the .env file
```

### 2. Start Infrastructure Services
```bash
# Infrastructure services only
docker-compose -f docker-compose-microservices.yml up -d eureka-server config-server api-gateway rabbitmq
```

### 3. Start Databases
```bash
# All databases
docker-compose -f docker-compose-microservices.yml up -d user-db portfolio-db trading-db market-db
```

### 4. Check Services
- **Eureka Dashboard**: http://localhost:8761 (admin/admin)
- **API Gateway**: http://localhost:8080
- **Config Server**: http://localhost:8888
- **RabbitMQ Management**: http://localhost:15672 (croesus/croesus123)

## 🔧 Development

### Maven Build
```bash
# Build entire project
mvn clean install

# Infrastructure only
mvn clean install -pl eureka-server,api-gateway,config-server

# Shared library only
mvn clean install -pl shared/common
```

### Docker Build
```bash
# Build infrastructure services
docker-compose -f docker-compose-microservices.yml build eureka-server config-server api-gateway
```

## 📊 Service Ports

| Service | Port | Description |
|---------|------|-------------|
| API Gateway | 8080 | Main entry point |
| Eureka Server | 8761 | Service Discovery |
| Config Server | 8888 | Configuration |
| RabbitMQ | 5672/15672 | Message Broker |
| User DB | 5433 | User database |
| Portfolio DB | 5434 | Portfolio database |
| Trading DB | 5435 | Trading database |
| Market DB | 5436 | Market database |

## 🔐 Security

- **JWT Authentication**: At API Gateway level
- **Database Isolation**: Each service has its own database
- **Environment Variables**: Sensitive information in .env file
- **CORS**: Configured for frontend

## 📈 Monitoring

### Health Checks
```bash
# Eureka Server
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health

# Config Server
curl http://localhost:8888/actuator/health
```

### Service Registry
- Eureka Dashboard: http://localhost:8761

## 🐛 Troubleshooting

### Service Registration Issues
1. Ensure Eureka Server is running
2. Check network connectivity
3. Verify services in Eureka Dashboard

### Database Connection Issues
1. Ensure PostgreSQL containers are running
2. Check for port conflicts
3. Verify environment variables

### RabbitMQ Issues
1. Check via RabbitMQ Management UI
2. Verify queues and exchanges
3. Validate connection credentials

## 🚀 Production Deployment

### Kubernetes
- Helm charts to be prepared
- ConfigMaps and Secrets to be used
- Horizontal Pod Autoscaling

### Monitoring
- Prometheus + Grafana
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Jaeger for distributed tracing

## 📝 Development Notes

### Next Steps
1. ✅ Infrastructure services completed
2. 🔄 User Service under development
3. 🔄 Portfolio Service to be developed
4. 🔄 Trading Service to be developed
5. 🔄 Market Service to be developed
6. 🔄 Frontend integration

### Best Practices
- Database per service pattern
- API versioning
- Circuit breaker pattern
- Distributed tracing
- Centralized logging

## 📄 License

MIT License - See LICENSE file for details. 