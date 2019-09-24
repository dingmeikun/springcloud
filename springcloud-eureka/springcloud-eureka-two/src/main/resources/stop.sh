#!/usr/bin/env bash
appname="springcloud-eureka-two"
version="1.0.0"
pid=$(ps aux | grep ${appname}-${version} | grep -v grep | awk '{print $2}')
echo $pid
kill $pid
#
