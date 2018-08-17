#!/usr/bin
# author yangzhao
# 如果使用jenkins 请在Post Steps 执行shell中添加 BUILD_ID=DONTKILLME
path=$(pwd)

jar_name="data_migration.jar"

server_port="10001"

start(){
    echo "启动 $jar_name 服务"
    mkdir -p $path/logs/gc
    nohup java -jar $jar_name --server.port=$server_port  -server -Duser.timezone=GMT+08 -Xms1024m -Xmx1024m -Xss256k -XX:MaxMetaspaceSize=128m -XX:MaxDirectMemorySize=512m -XX:MaxGCPauseMillis=100 -XX:ParallelGCThreads=4 \
    -XX:+UseAdaptiveSizePolicy -XX:+UseConcMarkSweepGC  -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=1 -XX:+CMSClassUnloadingEnabled \
    -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintClassHistogram -Djava.awt.headless=true -XX:+PrintGC -XX:+PrintGCDetails \
    -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:$path/logs/gc/$jar_name.log > $path/logs/app.log &
}

restart(){
    stop
    start
}

stop(){
    pid=`ps -ef|grep java | grep '$jar_name' | awk '{print $2}'`
    if [ $? -ne 0 ]; then
        echo "关闭 $jar_name 服务"
        kill -9 $pid
    fi


}

case "$1" in
    'start')
        start
        ;;
    'restart')
        restart
        ;;
    'stop')
        stop
        ;;
esac