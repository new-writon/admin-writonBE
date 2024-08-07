FROM amazoncorretto:17-alpine-jdk

ARG JAR_FILE=build/libs/writon-admin-0.0.1.jar

ADD ${JAR_FILE} writon-admin.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "/writon-admin.jar"]