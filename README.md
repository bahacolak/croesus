# Croesus - Digital Asset Management Platform

A modern cryptocurrency portfolio management and trading platform built with Spring Boot and React.

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Node.js 18+ (for frontend development)

### Development Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd croesus
```

2. **Setup environment variables**
```bash
cp env.example .env
# Edit .env file with your secure values
```

3. **Start backend + database (Docker)**
```bash
docker-compose up -d
```

4. **Start frontend (Host)**
```bash
cd frontend
npm install
npm start
```

5. **Access the application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Database: PostgreSQL @ localhost:5432

## 🔐 Security Configuration

### Environment Variables

Create a `.env` file from `env.example` and configure:

```bash
# Database (use strong passwords in production)
POSTGRES_PASSWORD=your_secure_password_here

# JWT (use strong secret in production)
JWT_SECRET=your_jwt_secret_key_here

# CORS (configure for your domain in production)
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

### Important Security Notes

⚠️ **Never commit sensitive data to Git**
- `.env` files are ignored by Git
- Use different secrets for development/production
- Rotate secrets regularly in production

## 🐳 Docker Commands

```bash
# Start services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and start
docker-compose up --build -d
```

## 📁 Project Structure

```
croesus/
├── src/                    # Spring Boot backend
├── frontend/               # React frontend
├── docker-compose.yml      # Docker services
├── Dockerfile              # Backend container
├── frontend/Dockerfile     # Frontend container
├── env.example             # Environment variables template
└── README.md
```

## 🛠️ Development

### Backend (Spring Boot)
- Framework: Spring Boot 3.4.5
- Database: PostgreSQL
- Security: JWT Authentication
- API: RESTful endpoints

### Frontend (React)
- Framework: React 18
- Styling: CSS + Styled Components
- Charts: Recharts
- HTTP Client: Axios

## 📊 Features

- Real-time cryptocurrency prices
- Portfolio management
- Trading interface
- Market analysis
- Secure authentication

## 🚀 Production Deployment

For production deployment:

1. Use strong, unique secrets in environment variables
2. Configure HTTPS/SSL
3. Set up proper database backups
4. Use production-grade container orchestration (Kubernetes)
5. Monitor logs and metrics

## 📄 License

MIT License - see LICENSE file for details 