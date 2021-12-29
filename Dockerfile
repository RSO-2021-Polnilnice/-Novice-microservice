FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/novice-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "novice-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "novice-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar novice-api-1.0.0-SNAPSHOT.jar