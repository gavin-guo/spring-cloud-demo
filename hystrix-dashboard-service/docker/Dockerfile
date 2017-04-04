FROM anapsix/alpine-java:8
VOLUME /tmp
ADD hystrix-dashboard-service-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8989
EXPOSE 8990
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]