# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Download dependencies
RUN chmod +x gradlew
RUN ./gradlew dependencies

# Copy source code
COPY src src

# Build the app
RUN ./gradlew build -x test

# Expose Render port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "build/libs/app.jar"]
