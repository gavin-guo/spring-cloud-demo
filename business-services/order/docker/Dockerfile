FROM openjdk:8-jre-alpine
ADD order-*.jar app.jar
EXPOSE 9105
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]