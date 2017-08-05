FROM openjdk:8-jre-alpine
ADD oauth2-server-*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]