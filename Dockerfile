FROM openjdk:17-jdk-alpine
MAINTAINER nurman.ng
# ADD target
COPY target/*jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
