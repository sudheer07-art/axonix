FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy gradle wrapper and config
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Download dependencies (cached)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN ./gradlew build -x test --no-daemon

EXPOSE 8080

# Run the generated jar (DO NOT rename)
CMD ["sh", "-c", "java -jar build/libs/*.jar"]
