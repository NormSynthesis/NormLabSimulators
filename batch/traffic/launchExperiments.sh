#!/bin/bash

SUFFIX=$1

source `dirname $0`/experimentSettings.sh
source `dirname $0`/experimentUtilities.sh
source `dirname $0`/config.sh

#----------------------------------------------------------------------------------------------------------------
# Main sequence
#----------------------------------------------------------------------------------------------------------------

# Remove previous files
if [ `ls | grep libs-todelete | wc -l` -gt 0 ]; then
	echo "Removing previous libs files..."
	rm libs-todelete*
fi
if [ `ls $SIM_BATCH_OUTPUT_DIR | grep TrafficDataOutput | wc -l` -gt 0 ]; then
	echo "Removing previous TrafficDataOutput.dat files..."
	rm $SIM_BATCH_OUTPUT_DIR/TrafficDataOutput.dat*
fi
if [ `ls $LOG_DIR | wc -l` -gt 0 ]; then
	echo "Removing previous logs..."
	rm $LOG_DIR/*
fi

# Simulations start
echo "Starting simulations..."
echo ""

# 1. Make new dir to put the experiments
outputDir=$SIM_BATCH_OUTPUT_DIR/`date +"%Y-%m-%d-%H-%M-%S"`
mkdir $outputDir

# 2. Execute the experiment N times with the given parameters
FinalNumExec=$(($InitialNumExec+$NumExecsForExperiment-1))
numExecs=`seq $InitialNumExec $FinalNumExec`

for numExec in $numExecs
do
  if [ $UseSequentialRandomSeeds ]; then
    SimRandomSeed="$numExec"l
  fi
	executeBatch $SimMap $SimNewCarsFreq $SimNumCarsToEmit $SimRandomSeed $SimMaxTicks $SimUseSemaphores $SimOnlySemaphores $SimNormViolationRate $NormsGenEffThreshold $NormsGenNecThreshold $NormsSpecEffThreshold $NormsSpecNecThreshold $NormsUtilityWindowSize $NormsDefaultUtility $NumTicksToConverge $NormsWeightCol $NormsWeightNoCol $NormsWeightFluidTraffic $numExec

  # Move the data file and the corresponding log
  mv $SIM_BATCH_OUTPUT_DIR/$outputDataFile $outputDir/"$outputDataFile.$numExec"
  mv $SIM_BATCH_OUTPUT_DIR/logs/*.log $outputDir/
  #rm logs/*.log
done

# 4. Compute averages of all the experiment data files
./computeAverages.sh $outputDir

# 5. Move norms file to its corresponding directory and create results directory
mkdir $outputDir/$resultsDir

mv $SIM_BATCH_OUTPUT_DIR/metrics.dat $outputDir/$resultsDir
mv $SIM_BATCH_OUTPUT_DIR/*.dat* $outputDir/$resultsDir

# 6. Plot results and move images to the results dir
#./plotData.sh "`pwd`/$dir" "averages.dat"
#mv "$dir/plot-averages.dat.png" "$dir/$resultsDir/plot-averages.png"
#mv $dir/*.png $dir/$resultsDir

# 7. Generate results file
#./generateResultsFile.sh $dir $totalNormsFile $finalNormsFile "$resultsFile.tex" "$sm" "$sncf" "$snce" "$svs" "$srs" "$smt" "$sus" "$sos" "$sol" "$nuv" "$nios" "$nex" "$nvp" "$nme" "$nmcna" "$nsws" "$nms" "$nwc" "$nwnc" "$nwft"

#echo "Generating results pdf..."
#echo ""

#pdflatex $dir/"$resultsFile.tex"
#mv "$resultsFile.pdf" $dir/$resultsDir
#mv "$resultsFile.aux" $dir
#mv "$resultsFile.log" $di

echo ""
echo "All jobs done!"


