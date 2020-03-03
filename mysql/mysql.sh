#!/bin/bash

FreeMem=`awk '/MemFree/ { print int($2/1024) }' /proc/meminfo`
HEAP="-Xms512m -Xmx2048m"
export timestamp=$(date +%Y%m%d_%H%M%S)
export jmeter_path=/opt/apache-jmeter-5.2.1
$JMETER_BIN/jmeter.sh \
 -Djava.net.preferIPv6Addresses=true \
 -n \
 -t ${jmeter_path}/mysql.jmx \
 -l ${jmeter_path}/tmp/apigeeload/jmeter.jtl \
 -j ${jmeter_path}/tmp/apigeeload/performance_${timestamp}.log
$JMETER_BIN/jmeter.sh \
 -g ${jmeter_path}/tmp/apigeeload/jmeter.jtl \
 -o ${jmeter_path}/tmp/apigeeload/results
echo "==== jmeter.log ===="
cat ${jmeter_path}/tmp/performance_${timestamp}.log
echo "==== Raw Test Report ===="
cat ${jmeter_path}/tmp/jmeter.jtl