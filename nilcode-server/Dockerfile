FROM openjdk:11
VOLUME /tmp
COPY target/nilcode-server-0.0.1-SNAPSHOT.jar nilcode.jar
RUN bash -c "touch /nilcode.jar"
EXPOSE 8443
ENTRYPOINT ["java","-jar","nilcode.jar"]
