# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy the parent POM
COPY pom.xml .
# Copy the user-service POM
COPY user-service/pom.xml ./user-service/
# Create a symlink to make Maven happy
RUN mkdir -p /root/.m2/repository/com/khmermarket/khmermarket-backend/1.0.0 && \
    ln -s /app/pom.xml /root/.m2/repository/com/khmermarket/khmermarket-backend/1.0.0/khmermarket-backend-1.0.0.pom

# Download all required dependencies into one layer
WORKDIR /app/user-service
RUN mvn dependency:go-offline -B

# Copy the source code
COPY user-service/src/ ./src/

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set the timezone (optional)
RUN apk add --no-cache tzdata
ENV TZ=Asia/Phnom_Penh

# Copy the built JAR file
COPY --from=build /app/user-service/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]