FROM openjdk:8-jre-alpine
ADD eureka-server-*.jar app.jar
EXPOSE 8761 8762
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]