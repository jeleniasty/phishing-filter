# ==========================
# Build stage
# ==========================
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle clean bootJar -x test --no-daemon

# ==========================
# Runtime stage
# ==========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENV GOOGLE_API_KEY=""
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]