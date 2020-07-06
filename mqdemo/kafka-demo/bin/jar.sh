#!/usr/bin/env bash
#!/bin/bash
# 指定指定java环境变量
#export JAVA_HOME=/opt/jdk1.8.0_181/
#export PATH=$JAVA_HOME/bin:$PATH
#export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
#可运行jar包启动脚本,将脚本和jar程序放置在同级目录,自动搜寻.jar后缀的文件
# 可配置 jvm参数和远程debug配置-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
JAVA_OPS="-server -XX:+PrintCommandLineFlags -Xms9126M -Xmx9126M -Xmn6144M
  -Xloggc:/usr/local/app/gclog/app-gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M -XX:+PrintGCDetails -XX:+PrintGCCause
  -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/app/gclog/heap.hprof"
APP_NAME=`ls ./| grep .jar$`
function check {
    local pid=`ps aux | grep $APP_NAME|grep -v "grep" | grep  "java" | awk '{ print $2 }'`
    if [ -z $pid ];then
       echo 0
    else
      echo $pid
    fi
}
function isRun {
    local pid=$(check)
    if [ $pid -ne 0 ];then
        echo "$APP_NAME is runing,pid is $pid"
    else
	    echo "$APP_NAME not run"
    fi
}
function start {
    local pid=$(check)
    if [ $pid -ne 0 ];then
        echo "$APP_NAME is runing pid $pid"
	return 0
    fi
    nohup java -jar -Dloader.path=loader $JAVA_OPS $APP_NAME  > log.out 2>&1 &
    pid=$(check)
    echo "$APP_NAME pid is $pid"
}

function softStop {
    local pid=$(check)
    if [ $pid -ne 0 ] ;then
        kill -15 $pid
        num=0
        while [[ num -le 5 ]]; do
            sleep 1
            pid=$(check)
            if [ $pid -eq 0 ];then
                break;
            fi
            num=$[ $num +1 ]
        done
    fi
}
# 慎用 kill 9 如果kill -15 杀不死使用kill -9
function foreStop {
    local pid=$(check)
    if [ $pid -ne 0 ] ;then
        kill -9 $pid
    fi
}
function stop() {
    local pid=$(check)
    softStop
    if [ $pid -ne 0 ] ;then
        echo "$APP_NAME is runing,pid is $pid"
        echo "stop $APP_NAME  success"
    else
        sleep 1
        if [ $pid -ne 0 ] ;then
            foreStop
            echo "stop $APP_NAME  success"
        fi
    fi


}
function restart {
    stop
    start
}


case $1 in
    "start" ) start;;
    "stop" ) stop;;
    "isRun" ) isRun;;
    "restart" ) restart;;
    * )  echo "Usage: $0 {start|stop|isRun|restart}";;
esac
exit 0


