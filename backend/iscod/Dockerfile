
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline -q

# copi du code source
COPY src ./src

#on fait le jar
RUN mvn package -DskipTests -q

# runtime java
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# port de spring
EXPOSE 8080

# lancement du jar
ENTRYPOINT ["java", "-jar", "app.jar"]
