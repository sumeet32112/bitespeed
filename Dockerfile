# Use OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Set environment variables
ENV JAVA_HOME=/usr/local/openjdk-17
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

# Optional: Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Copy Maven wrapper & pom.xml separately for better caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (optional but speeds up rebuilds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the app
COPY . .

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (used by Render and local)
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/contact-finder-0.0.1-SNAPSHOT.jar"]
