FROM openjdk:8-jdk-alpine
COPY target/sample-web-app-1.0-SNAPSHOT-jar-with-dependencies.jar ./target/Application.jar
COPY src/main/static/* src/main/static/
ENTRYPOINT java -jar target/Application.jar