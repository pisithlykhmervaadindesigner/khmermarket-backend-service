# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy parent POM and user-service POM
COPY pom.xml .
COPY user-service/pom.xml ./user-service/

# Download all required dependencies into one layer
RUN mvn -f ./user-service/pom.xml dependency:go-offline -B

# Copy the source code
COPY . .

# Build the application with Spring Boot Maven Plugin
RUN mvn -f ./user-service/pom.xml clean package spring-boot:repackage -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
# The Spring Boot Maven plugin creates a *-exec.jar file
COPY --from=build /app/user-service/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application with JVM optimizations
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
