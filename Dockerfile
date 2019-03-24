FROM maven:3.6.0-jdk-8-alpine as build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package 


FROM tomcat:8
COPY --from=build /usr/src/app/target/springbootrocks.war springbootrocks.war  
EXPOSE 8089
ENTRYPOINT ["java","-war","springbootrocks.war"]
