FROM openjdk:11
LABEL maintainer="nurman.ng"
# ADD target

ENTRYPOINT ["java", "-jar", "app.jar"]
