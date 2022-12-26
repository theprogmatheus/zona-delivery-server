# Docker Build Stage
FROM adoptopenjdk/maven-openjdk11 AS build


# Build Stage
WORKDIR /opt/app

COPY ./ /opt/app
RUN mvn clean install -DskipTests


# Docker Build Stage
FROM adoptopenjdk/openjdk11

COPY --from=build /opt/app/target/*.jar app.jar

ENV PORT 8085

EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar"]