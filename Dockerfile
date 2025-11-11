# 1. build stage
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-jdk-alpine AS builder

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
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-jre-alpine

ENV TZ=Asia/Seoul
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]