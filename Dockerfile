FROM gradle:jdk17 AS BUILD
WORKDIR /app
COPY /src /app/src
COPY build.gradle intTest.gradle gradle.properties openapi.properties settings.gradle /app/
RUN gradle build -x intTest

FROM ghcr.io/graalvm/jdk:java17-21
COPY --from=BUILD /app/build/libs/*-all.jar /app/notes.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/notes.jar"]