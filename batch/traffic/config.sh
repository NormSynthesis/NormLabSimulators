#!/bin/bash

# Initial settings
SIM_DIR=/home/javi/Proyectos/Eclipse/NormLabSimulators
NSM_DIR=/home/javi/Proyectos/Eclipse/NormSynthesisMachine
SIM_BATCH_DIR=$SIM_DIR/batch/traffic
SIM_BATCH_OUTPUT_DIR=$SIM_DIR/output/traffic
ECLIPSE_HOME=/opt/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins/
JESS_LIB=$SIM_DIR/lib/jess7.1.2.jar

# Prepare CLASSPATH
CLASSPATH=.

TMP_LIBS=libs-todelete.txt
echo "" > $TMP_LIBS

# Add libraries to path
ECLIPSE_PLUGINS_ALL_JARS=`find $ECLIPSE_PLUGINS -type f -name '*.jar'`
for file in $ECLIPSE_PLUGINS_ALL_JARS
do
	echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add bin files to path
ECLIPSE_PLUGINS_ALL_BINS=`find $ECLIPSE_PLUGINS -type d -name '*.bin'`
for dire in $ECLIPSE_PLUGINS_ALL_BINS
do
	echo "CLASSPATH=\$CLASSPATH:$dile" >> $TMP_LIBS
done

# Add Traffic Simulator libs
echo "CLASSPATH=\$CLASSPATH:$JESS_LIB" >> $TMP_LIBS
SIM_LIBS=`find $SIM_DIR/lib -type f -name '*.jar'`
for file in $SIM_LIBS
do
	echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add NSM libs
NSM_LIBS=`find $NSM_DIR/lib -type f -name '*.jar'`
for file in $NSM_LIBS
do
	echo "CLASSPATH=\$CLASSPATH:$file" >> $TMP_LIBS
done

# Add source to path
echo "CLASSPATH=\$CLASSPATH:$NSM_DIR/bin" >> $TMP_LIBS
echo "CLASSPATH=\$CLASSPATH:$SIM_DIR/bin" >> $TMP_LIBS
echo "CLASSPATH=\$CLASSPATH:$SIM_DIR/bin-groovy" >> $TMP_LIBS

sort $TMP_LIBS > "$TMP_LIBS-sorted"

echo "export CLASSPATH=\$CLASSPATH" >> "$TMP_LIBS-sorted"

source "$TMP_LIBS-sorted"

export CLASSPATH=$CLASSPATH

rm $TMP_LIBS "$TMP_LIBS-sorted"

