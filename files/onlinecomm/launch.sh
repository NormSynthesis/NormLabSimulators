#!/bin/bash

# Initial settings
PROJECT_DIR=/Volumes/DATA/MASTER_IA/Thesis/workspace/NormLab
IRON_DIR=/Volumes/DATA/MASTER_IA/Thesis/workspace/NormSynthesisMachine
ECLIPSE_HOME=/Applications/Repast-Simphony-2.0/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins


# Prepare CLASSPATH
CLASSPATH=.

TMP_LIBS="libs_todelete.txt"
echo "" > $TMP_LIBS

# Add libraries to path (Eclipse, Repast)
ECLIPSE_PLUGINS_ALL_JARS=`find $ECLIPSE_PLUGINS -type f -name '*.jar'`
for file in $ECLIPSE_PLUGINS_ALL_JARS
do
	echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add IRON libs
IRON_LIBS=`find $IRON_DIR/lib -type f -name '*.jar'`
for file in $IRON_LIBS
do
echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add Virtual Communities Simulation libs
PROJECT_LIBS=`find $PROJECT_DIR/lib -type f -name '*.jar'`
for file in $PROJECT_LIBS
do
echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add sources to path
echo "CLASSPATH=\$CLASSPATH:$IRON_DIR/bin" >> $TMP_LIBS
echo "CLASSPATH=\$CLASSPATH:$PROJECT_DIR/bin" >> $TMP_LIBS

# Format Libraries (sort...)
sort $TMP_LIBS > "$TMP_LIBS-sorted"
echo "export CLASSPATH=\$CLASSPATH" >> "$TMP_LIBS-sorted"
source "$TMP_LIBS-sorted"
export CLASSPATH=$CLASSPATH
rm $TMP_LIBS "$TMP_LIBS-sorted"

# Launch simulation
pushd .
cd $PROJECT_DIR
echo "pwd= `pwd`"

CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs
popd
