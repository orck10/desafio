FROM openjdk:11
ADD .mvn .mvn
ADD src src
ADD pom.xml pom.xml
ADD mvnw mvnw
RUN chmod a+x mvnw
RUN ./mvnw clean install -DskipTests
RUN rm -r src
RUN ls -a

WORKDIR /target

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./Challenge-0.0.1-SNAPSHOT.jar"]
