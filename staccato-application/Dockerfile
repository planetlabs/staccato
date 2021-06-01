FROM amazoncorretto:11.0.7
MAINTAINER josh@federal.planet.com

ARG JAR_NAME
ENV JAR_NAME $JAR_NAME

#COPY target/${JAR_NAME} /${JAR_NAME}
ADD target/${JAR_NAME} /${JAR_NAME}

ENTRYPOINT ["java", "-jar", "/staccato-1.0.0.jar"]
#ENTRYPOINT java "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" -jar /$JAR_NAME