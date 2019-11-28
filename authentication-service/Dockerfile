FROM openjdk:8-jdk-alpine
COPY target/authentication-service-0.0.1-SNAPSHOT.jar authentication-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Djava.net.preferIPv4Stack=true","-jar","/authentication-app.jar"]