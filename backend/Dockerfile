# ── Stage 1: Build ──
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# pom.xml එක මුලින්ම copy කරලා dependencies download කරනවා.
# ඒ නිසා code එක වෙනස් වුනාම dependencies ආපහු download වෙන්නෙ නෑ (cache).
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ── Stage 2: Run ──
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]