#FROM maven:3.9-eclipse-temurin-26-alpine AS build
#WORKDIR /build
#COPY pom.xml .
#RUN mvn -q -B dependency:go-offline
#COPY src ./src
#RUN mvn -q -B clean package -DskipTests
#
#FROM eclipse-temurin:26-jre-alpine
#WORKDIR /app
#COPY --from=build /build/target/testbankrest.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM maven:3.9-eclipse-temurin-26-alpine AS build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

