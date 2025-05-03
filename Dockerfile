FROM eclipse-temurin:17-jdk

# Añadir bibliotecas de certificados y seguridad necesarias
RUN apt-get update && apt-get install -y libnss3 ca-certificates

WORKDIR /app

COPY target/empresa-0.0.1-SNAPSHOT.jar app.jar
COPY wallet /app/wallet

ENV TNS_ADMIN=/app/wallet

ENTRYPOINT ["java", "-jar", "app.jar"]