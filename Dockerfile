# Use a base image with Java installed
    FROM openjdk:17-jdk-slim

    # Copy the executable JAR file into the container
    ARG JAR_FILE=target/*.jar
    COPY ${JAR_FILE} app.jar

    # Expose the port your Spring Boot application listens on (e.g., 8080)
    EXPOSE 8080

    # Command to run the Spring Boot application when the container starts
    ENTRYPOINT ["java", "-jar", "app.jar"]
