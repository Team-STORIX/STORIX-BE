# 1. build stage
FROM amazoncorretto:17-alpine-jdk as builder

ENV TZ=Asia/Seoul
WORKDIR /app

COPY gradlew ./gradlew
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./

RUN ./gradlew dependencies

COPY . .

RUN ./gradlew build -x test --no-daemon

# 2. run stage
FROM amazoncorretto:17-jre-alpine

ENV TZ=Asia/Seoul
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]