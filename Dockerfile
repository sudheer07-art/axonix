# 1️⃣ Use official Java 17 image
FROM eclipse-temurin:17-jdk

# 2️⃣ Set working directory inside container
WORKDIR /app

# 3️⃣ Copy Gradle wrapper and config files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 4️⃣ Give execute permission & download dependencies
RUN chmod +x gradlew && ./gradlew dependencies

# 5️⃣ Copy source code
COPY src src

# 6️⃣ Build Spring Boot app
RUN ./gradlew build -x test

# 7️⃣ Expose application port
EXPOSE 8080

# 8️⃣ Run the jar
CMD ["java", "-jar", "build/libs/*.jar"]
