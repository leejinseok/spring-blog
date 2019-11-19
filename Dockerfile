FROM openjdk:8-jre
COPY build/libs/gs-spring-boot-0.1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]