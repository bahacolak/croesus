FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Market Service"

WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar market-service.jar

# Port
EXPOSE 8085

# Uygulama başlat
ENTRYPOINT ["java", "-jar", "market-service.jar"] 