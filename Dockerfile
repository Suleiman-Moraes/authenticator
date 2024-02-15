FROM ubuntu:24.04 AS build

RUN apt-get update \
    && apt-get install -y openjdk-17-jdk \
    && rm -rf /var/lib/apt/lists/*

COPY . .

RUN apt-get update \
    && apt-get install -y maven \
    && mvn clean install -DskipTests -Dtest=!com.moraes.authenticator.integration.* \
    && rm -rf /var/lib/apt/lists/*

FROM openjdk:17-alpine

EXPOSE 80

COPY --from=build /target/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
