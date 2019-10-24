#!/bin/bash
## controll script for V2 services.
source /etc/profile
export JAVA_HOME="${JAVA_HOME:-/opt/java}"
export JRE_HOME="${JAVA_HOME/jre}"
export LC_ALL="zh_CN.UTF-8"
export app_dir="$(cd $(dirname $0);pwd)"
export app_name="${app_dir##*/}"
export jar_name="$(ls ${app_dir}/*.jar | awk -F '/' '{print $NF}'|head -1)"
export log_dir="/logdata/${app_name}"
export pid_file="${app_name}.pid"
export jvm_mem='-Xms2048m -Xmx2048m -Xmn1024m'

start(){
    cd ${app_dir}
    if [ -s "${pid_file}"  ];then
        echo "Service is running. PID: $(cat ${pid_file})"
        exit 1
    fi
    case $1 in 
        rd)
            jvm_mem='-Xms256m -Xmx256m -Xmn128m'
            sed -i '/disconf.env/ c disconf.env=rd' disconf.properties
            ;;
        qa)
            jvm_mem='-Xms256m -Xmx256m -Xmn128m'
            sed -i '/disconf.env/ c disconf.env=qa' disconf.properties
            ;;
        update)
            #git reset --hard
            echo "Will update ..."
            git pull -q
            if [ $? -eq 0 ];then
                echo "Curren Branch is $(git branch | awk '$1 ~/*/ {print $2}') "
                git log -1
            else
                echo "Warning! update failed, stop and  exit." 
                exit 2
            fi
            ;;
        *)
            ;;
    esac
    ulimit -n 204800 204800
    export java_opts="-server ${jvm_mem}  -XX:SurvivorRatio=8 -XX:+UseAES -XX:+UseAESIntrinsics -XX:+UseParallelGC -XX:+DisableExplicitGC -Xloggc:${log_dir}/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${log_dir} -XX:+PrintTenuringDistribution"
    nohup ${JAVA_HOME}/bin/java ${java_opts} -jar ${jar_name} > /dev/null 2>&1 &
    echo $! > ${pid_file}
    ps -p $(cat ${pid_file} ) >/dev/null 2>&1
    if [ $? -eq 0 ];then 
        echo "Start succeed."
        echo "Service ${app_name} runing in ENV: $(awk -F '=' '/env/ {print $2}' disconf.properties)"
        exit 0
    else
        echo "Start failed!"
        exit 1
    fi
}

stop(){
    if [ -s ${pid_file} ];then 
        app_pid=$(cat ${pid_file})
        echo "Service ${app_name} pid : ${app_pid} ,will be killed"
        kill  -9 ${app_pid} && rm -f ${pid_file}
    else
        echo "Service ${app_name} NOT running!"
        exit 0
    fi
}

stat(){
    if [ -s ${pid_file} ];then 
        app_pid=$(cat ${pid_file})
        echo "Service ${app_name} is running, PID: ${app_pid} ."
    else
        echo "Service ${app_name} NOT running."
        exit 0
    fi
 }

log(){
    if [ -f ${log_dir}/${app_name}_running.log ];then
        tail -f ${log_dir}/${app_name}_running.log
    else
        echo "File ${log_dir}/${app_name}_running.log was not found, please check!"
        exit 1
    fi
}

case $1 in
    start)
        start $2
        ;;
    update)
        start update
        ;;
    stop|kill)
        stop
        ;;
    stat|status)
        stat
        ;;
    log)
        log
        ;;
    *)
        echo "Usage $0 <start|update|stop/kill|stat/status|log>, please. like: "
        echo "Example:  $0 start [rd|qa|online]"
        exit 0
esac
