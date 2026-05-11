# ─────────────────────────────────────────────
# 1. BUILD STAGE
# ─────────────────────────────────────────────
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY . .

RUN ./gradlew :realtime-gateway:bootJar --no-daemon


# ─────────────────────────────────────────────
# 2. RUNTIME STAGE
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/realtime-gateway/build/libs/*.jar app.jar

EXPOSE 8080

# FORCE dev profile exactly like your bootRun args
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=8080 --spring.profiles.active=dev"]