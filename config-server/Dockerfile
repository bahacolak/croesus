FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Config Server - Configuration Management"

WORKDIR /app

# Copy the JAR file
COPY target/*.jar config-server.jar

# Port for the service
EXPOSE 8888

# Start the application
ENTRYPOINT ["java", "-jar", "config-server.jar"] 