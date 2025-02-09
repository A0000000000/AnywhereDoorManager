FROM ubuntu:latest
LABEL authors="maoyanluo"

WORKDIR /ws

ADD https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz /ws
ADD https://download.oracle.com/java/23/latest/jdk-23_linux-x64_bin.tar.gz /ws

RUN tar -zxvf apache-maven-3.9.9-bin.tar.gz
RUN mkdir jdk && tar -zxvf jdk-23_linux-x64_bin.tar.gz -C jdk --strip-components 1

WORKDIR /ws/code

COPY pom.xml /ws/code

ENV JAVA_HOME=/ws/jdk
ENV MAVEN_HOME=/ws/apache-maven-3.9.9

ENV PATH=$PATH:${JAVA_HOME}/bin:${MAVEN_HOME}/bin

RUN mvn dependency:resolve

COPY src /ws/code/src

RUN mvn package

RUN cp target/AnywhereDoorManager-1.0-SNAPSHOT.jar /ws/AnywhereDoorManager-1.0-SNAPSHOT.jar

WORKDIR /ws

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/ws/AnywhereDoorManager-1.0-SNAPSHOT.jar"]