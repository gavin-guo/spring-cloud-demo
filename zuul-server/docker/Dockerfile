FROM openjdk:8-jre-alpine
ADD zuul-server-*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]