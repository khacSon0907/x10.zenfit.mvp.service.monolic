# ==============================================================================
# STAGE 1: Build Spring Boot application
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml trước để cache dependency
COPY pom.xml .

# Download dependency
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# ==============================================================================
# STAGE 2: Runtime image
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy jar từ builder
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown -R spring:spring /app

# Use non-root user
USER spring:spring

# Render thường dùng port 10000
EXPOSE 10000

# Start app
ENTRYPOINT ["sh", "-c", "java -Xms256m -Xmx512m -Dserver.port=${PORT:-10000} -jar app.jar"]