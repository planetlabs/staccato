FROM quay.io/boundlessgeo/java-alpine:8-jre-alpine
MAINTAINER jfix@boundlessgeo.com

ARG JAR_NAME
ENV JAR_NAME $JAR_NAME

COPY target/${JAR_NAME} /${JAR_NAME}

ENTRYPOINT java -jar /$JAR_NAME
#ENTRYPOINT java "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" -jar /$JAR_NAME