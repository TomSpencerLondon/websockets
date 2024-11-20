FROM ghcr.io/graalvm/graalvm-ce:latest

WORKDIR /app
COPY target/spring-websockets.jar spring-websockets.jar

EXPOSE 8080

CMD ["java", "-jar", "spring-websockets.jar"]
