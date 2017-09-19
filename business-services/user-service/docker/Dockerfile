FROM openjdk:8-jre-alpine
ADD user-service-*.jar app.jar
EXPOSE 9100
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]