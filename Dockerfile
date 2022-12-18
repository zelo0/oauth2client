FROM gradle:7.4-jdk-alpine AS builder
WORKDIR /build
COPY . .
RUN gradle clean build --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/build/libs/running-hi-0.0.1-SNAPSHOT.jar running.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "running.jar"]