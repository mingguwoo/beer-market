#!/bin/bash

set -e

JAR_FILE="beer-market.jar"
APP_NAME="beer-market"
APOLLO_ID="beer-market"

# 计算最大堆内存。MEM_REQUEST是k8s系统参数，表示容器的内存总量，单位为byte
MAX_HEAP=$(expr $MEM_REQUEST / 1024 / 1024 - 4096)
# JVM参数
JAVA_OPTS="-server -Xms${MAX_HEAP}m -Xmx${MAX_HEAP}m -XX:CompressedClassSpaceSize=256m -XX:MaxDirectMemorySize=1024m -Xss512k -XX:+UseG1GC"
# 增加内存溢出日志
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/dumpfile.hprof"

# 环境参数$K8S_ENV: dev/test/stg/prod
ENV=$K8S_ENV

# sky walking start
# APM collector config start
APM_JAR="/app/skywalking/agent/java/skywalking-agent.jar"
APM_COLLECTOR_IP="10.10.200.189:11800"
# dev/test
if [[ "$K8S_CLUSTER" == "aws-eu" ]]; then
  APM_COLLECTOR_IP=""
else
  APM_COLLECTOR_IP="10.10.200.189:11800"
fi

# stg/prod
if [[ "$K8S_ENV" == "stg" ]]; then
  if [[ "$K8S_CLUSTER" == "aws-eu" ]]; then
    APM_COLLECTOR_IP=""
  else
    APM_COLLECTOR_IP=""
  fi
elif [[ "$K8S_ENV" == "prod" ]]; then
  if [[ "$K8S_CLUSTER" == "aws-eu" ]]; then
    APM_COLLECTOR_IP=""
  else
    APM_COLLECTOR_IP=""
  fi
fi

# jacoco
if [[ "$K8S_ENV" == "dev" ]] || [[ "$K8S_ENV" == "test" ]] || [[ "$K8S_ENV" == "stg" ]]; then
 wget "" -O "jacocoagent.jar"
fi

#把jacoco agent启动命令加入java opts
if [[ "$K8S_ENV" == "dev" ]] || [[ "$K8S_ENV" == "test" ]] || [[ "$K8S_ENV" == "stg" ]]; then
 IP_ADD=$(ifconfig eth0 |grep 'inet '|awk -F " " '{print $2}')
 JAVA_OPTS="${JAVA_OPTS} -javaagent:jacocoagent.jar=includes=*,output=tcpserver,port=6300,address=${IP_ADD},append=false"
fi

JAVA_OPTS="${JAVA_OPTS} -javaagent:${APM_JAR}"
JAVA_OPTS="${JAVA_OPTS} -Xverify:none"
# 设置你的服务名
JAVA_OPTS="${JAVA_OPTS} -Dskywalking.agent.service_name=${APP_NAME}-${ENV}"
JAVA_OPTS="${JAVA_OPTS} -Dskywalking.collector.backend_service=${APM_COLLECTOR_IP}"
# sky walking end

#其他启动命令
JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=$K8S_ENV -Denv=$K8S_ENV"
JAVA_OPTS="${JAVA_OPTS} -Duser.timezone=GMT+08"
JAVA_OPTS="${JAVA_OPTS} -Ddruid.mysql.usePingMethod=false"
JAVA_OPTS="${JAVA_OPTS} -Dapp.id=${APOLLO_ID}"
JAVA_OPTS="${JAVA_OPTS} -Dapollo.meta=${APOLLO_META}"
JAVA_OPTS="${JAVA_OPTS} -Dapollo.bootstrap.enabled=true"
JAVA_OPTS="${JAVA_OPTS} -Dapollo.bootstrap.eagerLoad.enabled=true"
JAVA_OPTS="${JAVA_OPTS} -Dapollo.cache-dir=/data"
JAVA_OPTS="${JAVA_OPTS} -Dapollo.cluster=$IDC"

#流量回放
if [ "$K8S_ENV" == "test"  ] ; then
  curl -o simulator-agent.zip
  mkdir takin/
  unzip simulator-agent.zip -d takin/
  JAVA_OPTS="${JAVA_OPTS} -javaagent:takin/simulator-agent/simulator-launcher-instrument.jar"
  JAVA_OPTS="${JAVA_OPTS} -Dpradar.project.name=${INST_NAME}"
  JAVA_OPTS="${JAVA_OPTS} -Dpradar.project.env=${K8S_ENV}"
  JAVA_OPTS="${JAVA_OPTS} -Dsimulator.delay=10"
  JAVA_OPTS="${JAVA_OPTS} -Dsimulator.unit=SECONDS"
  JAVA_OPTS="${JAVA_OPTS} -Djdk.attach.allowAttachSelf=true"
  JAVA_OPTS="${JAVA_OPTS} --add-opens java.base/java.lang=ALL-UNNAMED"
  JAVA_OPTS="${JAVA_OPTS} --add-opens java.base/java.net=ALL-UNNAMED"
  JAVA_OPTS="${JAVA_OPTS} --add-opens java.base/java.security=ALL-UNNAMED"
fi

java  -XX:NativeMemoryTracking=summary -jar ${JAVA_OPTS} /app/${JAR_FILE}