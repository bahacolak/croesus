FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Eureka Server - Service Discovery"

WORKDIR /app

COPY target/*.jar eureka-server.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "eureka-server.jar"] 