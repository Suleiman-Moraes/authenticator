FROM maven:3.8.4-openjdk-17-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests -Dtest=!com.moraes.authenticator.integration.*

FROM openjdk:17-alpine

# Copy the current directory contents into the container at /app
COPY target/*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# # Define environment variables
# ENV JAVA_OPTS=""
# ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-"beterrabaprofile"}

# Run app.jar when the container launches
# -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE
ENTRYPOINT [ "java", "-jar", "app.jar" ]
