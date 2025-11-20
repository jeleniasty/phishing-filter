# ==========================
# Build stage
# ==========================
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

COPY src ./src

RUN gradle bootJar -x test --no-daemon

# ==========================
# Runtime stage
# ==========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/phishing-filter-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV GOOGLE_API_KEY=""
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]