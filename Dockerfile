# ==============================================================================
# STAGE 1: Build Spring Boot application
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml trước để cache dependency layer
COPY pom.xml .

# Tải trước dependencies để tối ưu thời gian build
RUN mvn dependency:go-offline -B || true

# Copy toàn bộ mã nguồn
COPY src ./src

# Build file JAR (bỏ qua tests trong quá trình đóng gói image)
RUN mvn clean package -DskipTests

# ==============================================================================
# STAGE 2: Lightweight, secure runtime image
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cài đặt tzdata và cấu hình múi giờ Việt Nam (Asia/Ho_Chi_Minh)
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime \
    && echo "Asia/Ho_Chi_Minh" > /etc/timezone

# Tạo user không có đặc quyền root để chạy ứng dụng an toàn
RUN addgroup -S zenfit && adduser -S zenfit -G zenfit

# Copy jar từ build stage
COPY --from=builder /app/target/*.jar app.jar

# Phân quyền cho user zenfit
RUN chown -R zenfit:zenfit /app

USER zenfit:zenfit

# Mở cổng mặc định (8080 chuẩn Spring/Cloud Run, 10000 cho Render)
EXPOSE 8080 10000

# Tối ưu cờ JVM cho container, tự động nhận diện RAM và hỗ trợ biến PORT
ENV TZ=Asia/Ho_Chi_Minh \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Khởi chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
