#!/bin/sh
PORT=$1
BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
PROJECT_NAME="@project.artifactId@";
MAIN_CLASS="$PROJECT_NAME-@project.version@.jar";
NACOS_SERVER_ADDR="@profiles.nacos.uri@";
NACOS_USERNAME="@profiles.nacos.username@"
NACOS_PASSWORD="@profiles.nacos.password@"
NACOS_GROUP="@profiles.nacos.group@"
NACOS_NAMESPACE="@profiles.nacos.namespace@"
JVM_MAX_MEMORY="@profiles.jvm.maxmemory@"
LOG_PATH="@profiles.log.path@$PROJECT_NAME"
GC_PATH=$LOG_PATH/$PORT"-gc.log"
HS_ERR_PATH=$LOG_PATH/$PORT"-hs_err.log"
HEAP_DUMP_PATH=$LOG_PATH/$PORT"-heap_dump.hprof"
TEMP_PATH=$LOG_PATH/temp/
SUCCESS=0
FAIL=9
if [ ! -n "$PORT" ]; then  
	echo $"Usage: $0 {port}"
	exit $FAIL
fi
if [ ! -d $LOG_PATH ]; 
then     
    mkdir -p $LOG_PATH; 
fi
if [ ! -d $TEMP_PATH ]; 
then     
    mkdir -p $TEMP_PATH; 
fi
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=$(which java)
    if [ ! -x "$JAVACMD" ] ; then
	  echo  "Error: JAVA_HOME is not defined correctly."
      exit $ERR_NO_JAVA
    fi
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "We cannot execute $JAVACMD"
  exit $ERR_NO_JAVA
fi

if [ -e "$BASEDIR" ]
then
  JAVA_OPTS="-Xms$JVM_MAX_MEMORY -Xmx$JVM_MAX_MEMORY -Xss256K -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=10 -XX:InitiatingHeapOccupancyPercent=35 -XX:SoftRefLRUPolicyMSPerMB=500 -XX:+UseStringDeduplication -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$GC_PATH -XX:+HeapDumpOnOutOfMemoryError -XX:ErrorFile=$HS_ERR_PATH -XX:HeapDumpPath=$HEAP_DUMP_PATH"
fi

CLASSPATH=$CLASSPATH_PREFIX:
EXTRA_JVM_ARGUMENTS=""

cd "$BASEDIR/boot";

echo "starting application $PROJECT_NAME......"
exec "$JAVACMD" $JAVA_OPTS \
				$EXTRA_JVM_ARGUMENTS \
				-Dapp.name="$PROJECT_NAME" \
				-Dapp.port="$PORT" \
				-Dbasedir="$BASEDIR" \
				-Djava.io.tmpdir=$TEMP_PATH \
				-Dnacos.server.addr="$NACOS_SERVER_ADDR" \
				-Dnacos.username="$NACOS_USERNAME" \
				-Dnacos.password="$NACOS_PASSWORD" \
				-Dnacos.group="$NACOS_GROUP" \
				-Dnacos.namespace="$NACOS_NAMESPACE" \
				-Dloader.path="file://$BASEDIR/conf,file://$BASEDIR/lib" \
				-jar $MAIN_CLASS \
				--server.port="$PORT" \
				> /dev/null 2>&1 &
				
for i in {1..60}
do
	jcpid=`ps -ef | grep -v "grep" | grep "$MAIN_CLASS" | grep "app.port=$PORT" | sed -n '1P' | awk '{print $2}'`
	if [ $jcpid ]; then
		echo "The $PROJECT_NAME start finished, PID is $jcpid"
		exit $SUCCESS
	else
		echo "starting the application .. $i"
		sleep 1
	fi
done
echo "$PROJECT_NAME start failure!"