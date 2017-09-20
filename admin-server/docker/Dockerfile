FROM openjdk:8-jre-alpine
ADD admin-server-*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]