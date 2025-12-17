## build
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## runtime
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar app.jar
#
#ENV DB_URL=""
#ENV DB_USER=""
#ENV DB_PASSWORD=""
#ENV JWT_SECRET=""
#ENV STRIPE_KEY=""
#ENV STRIPE_PUBLIC_KEY=""
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]
