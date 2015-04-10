#!/bin/bash

#Cluster settings

#PBS -N SimonVC-IOSU
#PBS -l pmem=4000mb
#PBS -l walltime=05:00:00
#PBS -l nodes=2:ppn=2
#PBS -V
#PBS -q default

# Initial settings
ECLIPSE_HOME=/gpfs/home2/iosu/Repast-Simphony-2.1/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins

WORKSPACE=/gpfs/home2/iosu/workspace
IRON_DIR=/gpfs/home2/iosu/workspace/NormSynthesisMachine
NORMLAB_DIR=$WORKSPACE/NormLabSimulators
PROJECT_DIR=$WORKSPACE/Simulators/NormLabSimulators_2
mkdir $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000
mkdir $NORMLAB_DIR/output/onlinecomm/norms
cd $NORMLAB_DIR
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher 'batch/onlinecomm/populations/Population10.xml' 
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher 5000 10 0.3 5000 Population10
cp $PROJECT_DIR/batch/onlinecomm/populations/Population10.xml $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000

for i in `seq 10`; do
  cd $PROJECT_DIR
  CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms400M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs
  cd $PROJECT_DIR/output/onlinecomm
  mv ExperimentOutputData $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-run$i.dat
  rm ExperimentOutputData* 
  mv norms/ $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-run$i-norms
  mkdir norms
done
mv Convergence.dat $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-Convergence.dat
mv ConvergedNormativeSystems $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-ConvergedNormativeSystems.dat
mv ConvergedNormativeSystems.plot $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-ConvergedNormativeSystems.plot
mv NotConvergedNormativeSystems $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-NotConvergedNormativeSystems.dat
mv NotConvergedNormativeSystems.plot $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-NotConvergedNormativeSystems.plot
mv FinalNorms $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-FinalNorms.dat
mv FinalNorms.plot $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-FinalNorms.plot
mv TotalNorms $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-TotalNorms.dat
mv TotalNorms.plot $NORMLAB_DIR/output/onlinecomm/QA_Content-5000_Agents-10_Viol-0.3_StopTick-5000/Population10-TotalNorms.plot
