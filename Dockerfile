# Use Maven to build the application
FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# Run Maven to build the Spring Boot app
RUN mvn clean install

# Use OpenJDK for the final image
FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar spring-boot-app.jar

# Run the Spring Boot app
CMD ["java", "-jar", "spring-boot-app.jar"]
