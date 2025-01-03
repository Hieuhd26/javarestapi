FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java", "-jar", "backend-service.jar"]
# tham so 3 trùng phia tren

EXPOSE 8080

# build image
# docker build --tag