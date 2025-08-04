FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests

# Use the same maven image to copy the built JAR file
FROM maven:3.9.4-eclipse-temurin-21-alpine
COPY --from=build /app/target/*.jar /app/app.jar

FROM eclipse-temurin:21-alpine

# Copy the built JAR file from the previous stage
COPY --from=1 /app/app.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# # Define environment variables
# ENV JAVA_OPTS=""
# ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-"beterrabaprofile"}

# Run app.jar when the container launches
# -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]
