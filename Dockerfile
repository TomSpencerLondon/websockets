FROM ghcr.io/graalvm/graalvm-ce:latest:ol8-java11-21.2.0
WORKDIR /app
COPY target/spring-websockets.jar spring-websockets.jar

# Expose port 8080 for the application
EXPOSE 8080

# Run the Spring Boot application with GraalVM's java
CMD ["java", "-jar", "spring-websockets.jar"]
