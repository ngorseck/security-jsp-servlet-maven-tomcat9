FROM maven:3.9.6-eclipse-temurin-17 as maven_builder

ENV HOME=/app

WORKDIR $HOME

ADD pom.xml $HOME

RUN ["mvn", "dependency:resolve"]

ADD . $HOME

RUN ["mvn","clean","install","-T","2C","-DskipTests=true"]


FROM bitnami/tomcat:9.0

COPY --from=maven_builder /app/target/security.war /opt/bitnami/tomcat/webapps/security.war

EXPOSE 8080

#docker build -t webjsp-tomcat:1.0.2 .
#docker run --name webjsp-tomcat -e TOMCAT_USERNAME=seckngor -e TOMCAT_PASSWORD=passer -p 8082:8080 -d webjsp-tomcat:1.0.2