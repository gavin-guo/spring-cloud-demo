FROM openjdk:8-jre-alpine
ADD delivery-service-*.jar app.jar
EXPOSE 9107
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]