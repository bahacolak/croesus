FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Eureka Server - Service Discovery"

WORKDIR /app

# Copy the JAR file
COPY target/*.jar eureka-server.jar

# Port for the service
EXPOSE 8761

# Start the application
ENTRYPOINT ["java", "-jar", "eureka-server.jar"] 