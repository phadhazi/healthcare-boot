# 1. Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy only the pom.xml first to cache dependency downloads
COPY pom.xml ./

# Install dependencies (offline mode)
RUN apk add --no-cache maven && mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the project (skip tests)
RUN mvn clean package -DskipTests

# 2. Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar to the runtime image
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
