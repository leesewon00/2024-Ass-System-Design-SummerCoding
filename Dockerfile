FROM openjdk:17
ARG JAR_FILE=build/libs/ass1-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} springboot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/springboot.jar"]