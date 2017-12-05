FROM openjdk:8-jre-alpine
ADD admin-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]