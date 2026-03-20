FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Maven wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY domain/ domain/
COPY application/ application/
COPY infrastructure/ infrastructure/
COPY presentation/ presentation/
COPY bootstrap/ bootstrap/

# Compile using our own maven wrapper
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/bootstrap/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]