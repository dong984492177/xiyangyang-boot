FROM centos:7.8.2003

ENV BASE_DIR="application" \
    JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk" \
    JAVA="/usr/lib/jvm/java-1.8.0-openjdk/bin/java" \
    JVM_XMS="1g" \
    JVM_XMX="1g" \
    JVM_XMN="1g" \
    JVM_MS="128m" \
    JVM_MMS="320m" \
    TIME_ZONE=Asia/Shanghai

WORKDIR /${BASE_DIR}

RUN set -x \
    && yum update -y \
    && yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel iputils nc libcurl \
    && ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo '$TIME_ZONE' > /etc/timezone \
    && yum clean all

COPY message-provider.jar .
COPY docker-entrypoint.sh .

RUN mkdir -p logs \
	&& cd logs \
	&& touch message-provider.log 
RUN chmod +x docker-entrypoint.sh

EXPOSE 28000

ENTRYPOINT ["./docker-entrypoint.sh"]
