#!/bin/bash

# Initial settings
PROJECT_DIR=/Users/Iosu/Documents/workspace/VirtualCommunitiesSimulation
ECLIPSE_HOME=/Applications/Repast-Simphony-2.0/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
JESS_LIB=$PROJECT_DIR/lib/jess7.1.2.jar
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins/

# Prepare CLASSPATH
CLASSPATH=.

TMP_LIBS="libs_todelete.txt"
echo "" > $TMP_LIBS

# Add libraries to path
ECLIPSE_PLUGINS_ALL_JARS=`find $ECLIPSE_PLUGINS -type f -name '*.jar'`

for file in $ECLIPSE_PLUGINS_ALL_JARS
do
	echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add JESS lib
echo "CLASSPATH=\$CLASSPATH:$JESS_LIB" >> $TMP_LIBS

# Add source to path
echo "CLASSPATH=\$CLASSPATH:$PROJECT_DIR/bin" >> $TMP_LIBS
echo "CLASSPATH=\$CLASSPATH:$PROJECT_DIR/bin-groovy" >> $TMP_LIBS

sort $TMP_LIBS > "$TMP_LIBS-sorted"

echo "export CLASSPATH=\$CLASSPATH" >> "$TMP_LIBS-sorted"

source "$TMP_LIBS-sorted"

export CLASSPATH=$CLASSPATH

rm $TMP_LIBS "$TMP_LIBS-sorted"

# TEST

pushd .
cd $PROJECT_DIR
echo "pwd= `pwd`"

CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain  $PROJECT_DIR/virtualCommunities.rs 

popd
