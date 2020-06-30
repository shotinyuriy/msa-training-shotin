FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar","/app.jar"]