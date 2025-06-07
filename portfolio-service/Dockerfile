FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Portfolio Service"

WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar portfolio-service.jar

# Port
EXPOSE 8082

# Uygulama başlat
ENTRYPOINT ["java", "-jar", "portfolio-service.jar"] 