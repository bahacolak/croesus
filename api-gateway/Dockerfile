FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus API Gateway - Single Entry Point"

WORKDIR /app

COPY target/*.jar api-gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-gateway.jar"] 