# --- Étape de build : compile et package le jar (tests exécutés en CI, pas ici) ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# --- Étape d'exécution : JRE seule, image légère ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/quote-part-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
