## Użyj oficjalnego obrazu Maven do budowy projektu
#FROM maven:3.9.4-eclipse-temurin-17 AS build
#
## Ustawienie katalogu roboczego dla procesu budowy
#WORKDIR /app
#
## Kopiowanie plików projektu do obrazu
#COPY pom.xml ./
#COPY src ./src
#
## Budowanie aplikacji
#RUN mvn clean package -DskipTests
#
## Użycie lekkiego obrazu JDK do uruchamiania aplikacji
#FROM eclipse-temurin:17-jre-alpine
#
## Ustawienie katalogu roboczego dla aplikacji
#WORKDIR /app
#
## Kopiowanie gotowego pliku JAR z etapu budowy
#COPY --from=build /app/target/*.jar app.jar
#
## Otwórz port dla aplikacji
#EXPOSE 8080
#
## Uruchamianie aplikacji Spring Boot
#ENTRYPOINT ["java", "-jar", "app.jar"]
