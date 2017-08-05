FROM openjdk:8-jre-alpine
ADD notification-service-*.jar app.jar
EXPOSE 9201
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]