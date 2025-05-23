# Use a JDK image to build the application
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /apigateway
WORKDIR /apigateway
RUN gradle build --no-daemon

# Use a smaller JRE image to run the application
FROM eclipse-temurin:17-jre
EXPOSE 8081
COPY --from=build /apigateway/build/libs/apigateway-0.0.1-SNAPSHOT.jar apigateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","apigateway-0.0.1-SNAPSHOT.jar"]
