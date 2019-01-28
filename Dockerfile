FROM openjdk:8-jdk
VOLUME /tmp

RUN apt-get update && apt-get install -y \
   unzip \
   curl \
   net-tools \
   telnet \
   git

ADD build/libs/propeler-service-0.1.0-SNAPSHOT.jar propeler-service.jar

ENTRYPOINT ["java","-jar","/propeler-service.jar","-Djava.security.egd=file:/dev/./urandom"]