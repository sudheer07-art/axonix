# Use Java 17
FROM eclipse-temurin:17-jdk

# Set work directory
WORKDIR /app

# Copy everything
COPY . .

# Fix gradlew permission
RUN chmod +x gradlew

# Build the app
RUN ./gradlew clean build -x test

# Expose port
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "build/libs/*.jar"]
