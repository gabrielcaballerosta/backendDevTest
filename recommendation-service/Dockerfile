FROM openjdk:17-alpine

WORKDIR /app

ARG PROJECT_VERSION=1.0-SNAPSHOT
ARG JAR_FILE=target/recommendation-service-${PROJECT_VERSION}.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar