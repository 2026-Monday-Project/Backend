# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew

COPY src ./src

# 테스트 중 StoryServiceConcurrencyTest 등은 로컬 MySQL 접속이 필요해 이미지 빌드 환경에서는
# 실행할 수 없다. 테스트는 별도로(로컬/CI) 돌리고, 이미지 빌드에서는 스킵한다.
RUN ./gradlew clean build -x test --no-daemon \
    && rm -f build/libs/*-plain.jar

# ---- Run stage ----
FROM eclipse-temurin:21-jre AS run
WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=build /app/build/libs/monday-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
