FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy target jar into container (rename as app.jar)
COPY target/WealthTrack-0.0.1-SNAPSHOT.jar wealthTrack-v1.0.jar

# Expose port (default Spring Boot port)
EXPOSE 8080

# Start command
ENTRYPOINT ["java", "-jar", "wealthTrack-v1.0.jar"]