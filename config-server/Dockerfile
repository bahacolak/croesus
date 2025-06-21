FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Config Server - Configuration Management"

WORKDIR /app

COPY target/*.jar config-server.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "config-server.jar"] 