FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus API Gateway - Single Entry Point"

WORKDIR /app

# Copy the JAR file
COPY target/*.jar api-gateway.jar

# Port for the service
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "api-gateway.jar"] 