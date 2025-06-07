FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus User Service"

WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar user-service.jar

# Port
EXPOSE 8081

# Uygulama başlat
ENTRYPOINT ["java", "-jar", "user-service.jar"] 