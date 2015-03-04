#!/bin/bash

#Cluster settings

#PBS -N SimonVC-IOSU
#PBS -l pmem=4000mb
#PBS -l walltime=72:00:00
#PBS -l nodes=2:ppn=2
#PBS -V
#PBS -q default

# Initial settings
ECLIPSE_HOME=/home/iosu/Repast-Simphony-2.1/eclipse
JAVA_EXECUTABLE=java
JAVA_FLAGS=-client
ECLIPSE_PLUGINS=$ECLIPSE_HOME/plugins

WORKSPACE=/home/iosu/workspace
IRON_DIR=/home/iosu/workspace/NormSynthesisMachine
NORMLAB_DIR=$WORKSPACE/NormLabSimulators
PROJECT_DIR=$WORKSPACE/Simulators/NormLabSimulators_54
mkdir $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000
mkdir $NORMLAB_DIR/output/onlinecomm/norms
cd $NORMLAB_DIR
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.PopulationSwitcher $NORMLAB_DIR'/batch/onlinecomm/populations/Population6.xml' $PROJECT_DIR'/files/onlinecomm/populations/population.xml' 
java -cp $PROJECT_DIR/bin es.csic.iiia.normlab.onlinecomm.batch.BatchParamsSwitcher 5000 100 0.7 5000 Population6 2 1.0 1.0 -0.9 -0.9 0.05 0.1 0.025 20.0 100.0 0.5 1000.0 true false $PROJECT_DIR'/batch/onlinecomm/batch_params.xml'
cp $PROJECT_DIR/batch/onlinecomm/populations/Population6.xml $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000

echo "Launching simulations for population 'Population6'"
for i in `seq 10`; do
  cd $PROJECT_DIR
  echo "  Simulation number" $i
  CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xms1024M -Xmx1g repast.simphony.batch.BatchMain -params $PROJECT_DIR/batch/onlinecomm/batch_params.xml $PROJECT_DIR/repast-settings/OnlineCommunity.rs > $NORMLAB_DIR/output/onlinecomm/Population6-Run$i.log
  cd $PROJECT_DIR/output/onlinecomm
  mv ExperimentOutputData $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-run$i.dat
  rm ExperimentOutputData* 
  mv norms/ $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-run$i-norms
  mv $NORMLAB_DIR/output/onlinecomm/Population6-Run$i.log $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000
  mkdir norms
done
mv Convergence.dat $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-Convergence.dat
mv ComputationMetrics.dat $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-ComputationMetrics.dat
mv ConvergedNormativeSystems $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-ConvergedNormativeSystems.dat
mv ConvergedNormativeSystems.plot $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-ConvergedNormativeSystems.plot
mv NotConvergedNormativeSystems $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-NotConvergedNormativeSystems.dat
mv NotConvergedNormativeSystems.plot $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-NotConvergedNormativeSystems.plot
mv FinalNorms $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-FinalNorms.dat
mv FinalNorms.plot $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-FinalNorms.plot
mv TotalNorms $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-TotalNorms.dat
mv TotalNorms.plot $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6-TotalNorms.plot
mv *.log $NORMLAB_DIR/output/onlinecomm/100WID-NSStrategy-SIMON_Content-5000_Agents-100_Viol-0.7_StopTick-5000/Population6
