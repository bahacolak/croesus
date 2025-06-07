FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Trading Service"

WORKDIR /app

# Copy the JAR file
COPY target/*.jar trading-service.jar

# Port for the service
EXPOSE 8084

# Start the application
ENTRYPOINT ["java", "-jar", "trading-service.jar"] 