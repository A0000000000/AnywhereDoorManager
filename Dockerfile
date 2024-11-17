FROM ubuntu:latest
LABEL authors="maoyanluo"

WORKDIR /ws

ADD apache-maven-3.9.9-bin.tar.gz /ws
ADD jdk-23_linux-x64_bin.tar.gz /ws

WORKDIR /ws/code

ADD src /ws/code/src
ADD pom.xml /ws/code

ENV JAVA_HOME=/ws/jdk-23.0.1
ENV MAVEN_HOME=/ws/apache-maven-3.9.9

ENV PATH=$PATH:${JAVA_HOME}/bin:${MAVEN_HOME}/bin

RUN mvn package

RUN cp target/AnywhereDoorManager-1.0-SNAPSHOT.jar /ws/AnywhereDoorManager-1.0-SNAPSHOT.jar

WORKDIR /ws

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/ws/AnywhereDoorManager-1.0-SNAPSHOT.jar"]