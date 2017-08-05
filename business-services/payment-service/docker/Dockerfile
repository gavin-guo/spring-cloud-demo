FROM openjdk:8-jre-alpine
ADD payment-service-*.jar app.jar
EXPOSE 9106
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]