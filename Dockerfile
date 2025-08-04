FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/votacao-api.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]