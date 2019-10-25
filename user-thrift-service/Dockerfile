FROM openjdk:8u222-jre
MAINTAINER zzg 975129758@qq.com

COPY target/user-thrift-service-1.0-SNAPSHOT.jar /user-service.jar

ENTRYPOINT ["java", "-jar", "/user-service.jar"]