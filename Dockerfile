FROM adoptopenjdk/openjdk16:alpine

RUN apk --no-cache add maven

WORKDIR /app

COPY pom.xml .

RUN mvn -B dependency:resolve dependency:resolve-plugins

COPY . .

RUN mvn -B clean package

EXPOSE 8080

CMD ["java", "-jar", "target/test-0.0.1-SNAPSHOT.jar"]

