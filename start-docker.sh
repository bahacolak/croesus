#!/bin/bash

echo "🐳 Starting Croesus application with Docker..."

# Check Docker and Docker Compose
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

echo "✅ Docker and Docker Compose are ready"

# Stop and clean existing containers if any
echo "🧹 Cleaning up existing containers..."
docker-compose down

# Start containers
echo "🚀 Starting containers..."
docker-compose up --build -d

echo "⏳ Services are starting... (waiting 30 seconds)"
sleep 30

echo "✅ Croesus application is ready!"
echo ""
echo "📱 Frontend: http://localhost:3000"
echo "🖥️  Backend:  http://localhost:8080"
echo "🗄️  Database: PostgreSQL localhost:5432"
echo ""
echo "📊 To check container status: docker-compose ps"
echo "📝 To view logs: docker-compose logs -f"
echo "🛑 To stop: docker-compose down" 