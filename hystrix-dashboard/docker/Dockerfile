FROM openjdk:8-jre-alpine
ADD hystrix-dashboard-*.jar app.jar
EXPOSE 8989 8990
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]