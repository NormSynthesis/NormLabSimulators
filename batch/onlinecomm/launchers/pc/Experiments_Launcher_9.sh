#!/bin/bash

# Initial settings
ECLIPSE_HOME=/home/iosu/Repast-Simphony-2.1/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins

WORKSPACE=/home/iosu/workspace/NEW-SIMON
IRON_DIR=$WORKSPACE/NormSynthesisMachine
NORMLAB_DIR=$WORKSPACE/NormLabSimulators
PROJECT_DIR=$NORMLAB_DIR

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

mkdir $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000
mkdir $NORMLAB_DIR/output/onlinecomm/norms
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher 'batch/onlinecomm/populations/Population2.xml' 
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher 5000 100 0.7 5000 Population2
cp batch/onlinecomm/populations/Population2.xml $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000

for i in `seq 10`; do
  cd $PROJECT_DIR
  CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms400M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs
  cd $PROJECT_DIR/output/onlinecomm
  mv ExperimentOutputData $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-run$i.dat
  rm ExperimentOutputData* 
  mv norms/ $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-run$i-norms
  mkdir norms
done
mv Convergence.dat $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-Convergence.dat
mv ConvergedNormativeSystems $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-ConvergedNormativeSystems.dat
mv ConvergedNormativeSystems.plot $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-ConvergedNormativeSystems.plot
mv FinalNorms $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-FinalNorms.dat
mv FinalNorms.plot $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-FinalNorms.plot
mv TotalNorms $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-TotalNorms.dat
mv TotalNorms.plot $NORMLAB_DIR/output/onlinecomm/NEW-SIMON100_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population2-TotalNorms.plot
