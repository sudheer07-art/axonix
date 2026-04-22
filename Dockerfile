# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# App folder
WORKDIR /app

# Copy Gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Give permission
RUN chmod +x gradlew

# Download dependencies first (cache faster builds)
RUN ./gradlew dependencies --no-daemon

# Copy project files
COPY src src

# Build jar
RUN ./gradlew bootJar --no-daemon

# Expose port
EXPOSE 10000

# Run app
CMD ["java","-jar","build/libs/app.jar"]