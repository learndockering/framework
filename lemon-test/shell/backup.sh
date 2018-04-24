#!/usr/bin/env bash

TS=`date +%Y%m%d%H%M%S`

if [ -z "${LEMON_HOME}" ]; then
  LEMON_HOME="$(cd `dirname $0`;cd ..;pwd)"
fi
JARS=$(ls $LEMON_HOME/*.jar)
for JAR in $JARS
do
  JAR_NM=$(echo $JAR|awk -F "/" '{print $NF}')
  JAR_BAK=${JAR_NM}"_"${TS}
  mv $JAR ${LEMON_HOME}/backup/${JAR_BAK} 
  echo "正在备份${JAR_NM}...."
done
echo "备份jar完成"
