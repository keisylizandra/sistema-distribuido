FROM maven:3.9-eclipse-temurin AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package

# Etapa 2: runtime mais leve
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/classes ./classes
EXPOSE 1234

CMD ["java", "-cp", "classes", "sd.Servidor"]