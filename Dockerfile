FROM openjdk:16-slim

EXPOSE 8080

WORKDIR /app

COPY target/code-challenge-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]