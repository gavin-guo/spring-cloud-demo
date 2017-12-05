FROM openjdk:8-jre-alpine
ADD notification-*.jar app.jar
EXPOSE 9101
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]