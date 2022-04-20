#!/bin/bash

set -x
# app name
SERVICE_NAME=message-provider

#===========================================================================================
# JVM Configuration
#===========================================================================================
# JAVA_OPTS="${JAVA_OPTS} -Xms512m -Xmx512m -Xmn256m"
JAVA_OPTS="${JAVA_OPTS} -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=9 -XX:+DisableExplicitGC -XX:+ScavengeBeforeFullGC"
JAVA_OPTS="${JAVA_OPTS} -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+ExplicitGCInvokesConcurrent -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow  -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="${JAVA_OPTS} -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai"
JAVA_OPTS="${JAVA_OPTS} -Dconsole.home=${BASE_DIR}"
JAVA_OPTS="${JAVA_OPTS} -jar /${BASE_DIR}/${SERVICE_NAME}.jar"


echo "console is starting,you can check the /${BASE_DIR}/logs/${SERVICE_NAME}.log"
echo "$JAVA ${JAVA_OPTS}" >> /${BASE_DIR}/logs/${SERVICE_NAME}.log 2>&1 &
nohup $JAVA ${JAVA_OPTS} >> /${BASE_DIR}/logs/${SERVICE_NAME}.log 2>&1 < /dev/null
