FROM openjdk:8u141-jre
VOLUME /tmp
WORKDIR /fabfund
COPY target/fabcar-application-0.0.1-SNAPSHOT.jar app.jar
COPY my-network my-network
COPY wallet wallet
EXPOSE 8080
ENTRYPOINT exec java -jar app.jar -Xms256m -Xmx1024m
