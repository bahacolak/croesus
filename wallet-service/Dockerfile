FROM openjdk:17

LABEL maintainer="Bahadir Colak"
LABEL description="Croesus Wallet Service"

WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar wallet-service.jar

# Port
EXPOSE 8083

# Uygulama başlat
ENTRYPOINT ["java", "-jar", "wallet-service.jar"] 