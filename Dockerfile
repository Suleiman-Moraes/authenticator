FROM ubuntu:24.04 AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests -Dtest=!com.moraes.authenticator.integration.*

FROM openjdk:17-alpine

EXPOSE 80

COPY --from=build /target/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]

