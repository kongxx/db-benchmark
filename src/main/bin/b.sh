#!/bin/sh

BIN_DIR=`dirname $0`
BIN_PATH=`cd $BIN_DIR ; pwd`
DB_BENCHMARK_HOME=`cd $BIN_DIR/../ ; pwd`

jarfiles=`find ${DB_BENCHMARK_HOME}/lib -name "*" -print`
for jarfile in $jarfiles
do
	LCP=${jarfile}:${LCP}
done

"$JAVA_HOME"/bin/java -Xms512m -Xmx1024m -DDB_BENCHMARK_HOME=$DB_BENCHMARK_HOME -cp ${LCP} db.benchmark.Main $*