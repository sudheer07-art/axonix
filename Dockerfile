FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Gradle wrapper and configs
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Fix permissions
RUN chmod +x gradlew

# Download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN ./gradlew build -x test --no-daemon

EXPOSE 8080

#CMD ["java", "-jar", "build/libs/app.jar"]
CMD ["java","-Xms128m","-Xmx256m","-jar","app.jar"]


