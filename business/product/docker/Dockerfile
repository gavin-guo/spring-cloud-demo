FROM openjdk:8-jre-alpine
ADD product-*.jar app.jar
EXPOSE 9102
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]