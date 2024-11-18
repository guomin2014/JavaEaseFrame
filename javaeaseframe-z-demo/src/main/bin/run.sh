#!/bin/sh

RETVAL=$?

#. /etc/profile #加载环境变量（Jenkins远程启动程序报找不到Java文件问题，需加载环境变量）

export CATALINA_BASE="$PWD"

if [[ $CATALINA_BASE == /usr/* || $CATALINA_BASE == /home/* || $CATALINA_BASE == /web/* ]];then
  echo "load profile..."
  CATALINA_BASE=`dirname $0` #获取命令执行文件的绝对路径
  . /etc/profile #加载环境变量（Jenkins远程启动程序报找不到Java文件问题，需加载环境变量）
fi

case "$1" in
start)
if [ -f $CATALINA_BASE/start.sh ];then
$CATALINA_BASE/start.sh 11080
fi
;;
stop)
if [ -f $CATALINA_BASE/stop.sh ];then
$CATALINA_BASE/stop.sh 11080
fi
;;
*)
echo $"Usage: $0 {start|stop}"
exit 1
;;
esac
exit $RETVAL