source `dirname $0`/config.sh

BATCH_CMD=`dirname $0`/batch.sh
LOG_DIR=$SIM_BATCH_OUTPUT_DIR/logs

# Max number of executions of the same configuration in case there are errors
MAX_EXECS_SAME_CONFIG_ON_ERRORS=2

# Number of executions of the same configuration to perform an average
REPEAT_SAME_CONFIG=1

#----------------------------------------------------------------------------------------------------------------
# Creates a param file to be passed to Repast
#----------------------------------------------------------------------------------------------------------------

function createParamsFile()
{
	local fileName=$1

	#TrafficSim
	local SimMap=$2
	local SimNewCarsFreq=$3
	local SimNumCarsToEnter=$4
	local SimRandomSeed=$5
	local SimMaxTicks=$6
	local SimUseSemaphores=$7
	local SimOnlySemaphores=$8
	local SimNormViolationRate=$9

	#NSM 
	local NormsGenEffThreshold=${10}
	local NormsGenNecThreshold=${11}
	local NormsSpecEffThreshold=${12}
	local NormsSpecNecThreshold=${13}
	local NormsPerfRangeSize=${14}
	local NormsDefaultUtility=${15}
	local NumTicksToConverge=${16}
	local NormsWeightCol=${17}
	local NormsWeightNoCol=${18}
	local NormsWeightFluidTraffic=${19}
	local NumExec=${20}

	# Write XML parameter file
	echo "<?xml version='1.0'?>" > $fileName
	echo "<sweep runs='1'>"      >> $fileName
	echo "  <parameter name='SimMap'                   	value='$SimMap'				type='constant' constant_type='number' />" >>  $fileName
	echo "  <parameter name='SimNewCarsFreq'           	value='$SimNewCarsFreq'			type='constant' constant_type='number' />" >>  $fileName
	echo "  <parameter name='SimNumCarsToEnter'         	value='$SimNumCarsToEnter'		type='constant' constant_type='number' />" >>  $fileName
	echo "  <parameter name='SimRandomSeed'            	value='$SimRandomSeed'			type='constant' constant_type='number' />" >>  $fileName
	echo "  <parameter name='SimMaxTicks'              	value='$SimMaxTicks'			type='constant' constant_type='number' />" >> $fileName	
	echo "  <parameter name='SimGUI'                   	value='false'				type='constant' constant_type='boolean' />" >>  $fileName
	echo "  <parameter name='SimUseSemaphores'         	value='$SimUseSemaphores'		type='constant' constant_type='boolean' />" >> $fileName	
	echo "  <parameter name='SimOnlySemaphores'        	value='$SimOnlySemaphores'		type='constant' constant_type='boolean' />" >> $fileName	
	echo "  <parameter name='SimNormViolationRate'     	value='$SimNormViolationRate'		type='constant' constant_type='number' />" >> $fileName	

	echo "  <parameter name='NormsGenEffThreshold'		value='$NormsGenEffThreshold'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsGenNecThreshold'          value='$NormsGenNecThreshold'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsSpecEffThreshold'       	value='$NormsSpecEffThreshold'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsSpecNecThreshold'        	value='$NormsSpecNecThreshold'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsPerfRangeSize'        	value='$NormsPerfRangeSize'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsDefaultUtility'        	value='$NormsDefaultUtility'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NumTicksToConverge'     	value='$NumTicksToConverge'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsWeightCol'      		value='$NormsWeightCol'			type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsWeightNoCol'             	value='$NormsWeightNoCol'		type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsWeightFluidTraffic'     	value='$NormsWeightFluidTraffic'	type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NumExec'		     	value='$NumExec'			type='constant' constant_type='number' />" >> $fileName

	echo "  <parameter name='NormsSpecThresholdEpsilon'    	value='0.025'				type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormsMinEvaluations'	     	value='25'				type='constant' constant_type='number' />" >> $fileName

	echo "  <parameter name='NormSynthesisExample'	     	value='0'				type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormSynthesisStrategy'		value='3'				type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormGeneralisationMode'     	value='1'				type='constant' constant_type='number' />" >> $fileName
	echo "  <parameter name='NormGeneralisationStep'    	value='1'				type='constant' constant_type='number' />" >> $fileName

	echo "</sweep>" >> $fileName

	cp $fileName $SIM_DIR
}

#----------------------------------------------------------------------------------------------------------------
# Executes Repast with the passed parameters
#----------------------------------------------------------------------------------------------------------------

function executeBatch()
{
	local SimMap=$1
	local SimNewCarsFreq=$2
	local SimNumCarsToEmit=$3
	local SimRandomSeed=$4
	local SimMaxTicks=$5
	local SimUseSemaphores=$6
	local SimOnlySemaphores=$7
	local SimNormViolationRate=$8

	#NSM 
	local NormsGenEffThreshold=$9
	local NormsGenNecThreshold=${10}
	local NormsSpecEffThreshold=${11}
	local NormsSpecNecThreshold=${12}
	local NormsUtilityWindowSize=${13}
	local NormsDefaultUtility=${14}
	local NumTicksToConverge=${15}
	local NormsWeightCol=${16}
	local NormsWeightNoCol=${17}
	local NormsWeightFluidTraffic=${18}

	local NumExec=${19}

	local paramsFile="$LOG_DIR/tmpParams.xml"

	local logFile=$LOG_DIR/`date +"%Y-%m-%d-%H-%M-%S"`-"|TRAFFIC_SIM|Map-$SimMap|NCarFr-$SimNewCarsFreq|NCarEm-$SimNumCarsToEmit|Seed-$SimRandomSeed|MaxTicks-$SimMaxTicks|ViolRate-$SimNormViolationRate|IRON|GenEffThr-$NormsGenEffThreshold|GenNecThr-$NormsGenNecThreshold|SpecEffThr-$NormsSpecEffThreshold|SpecNecThr-$NormsSpecNecThreshold|UWdwSz-$NormsUtilityWindowSize|ConvTck-$NumTicksToConverve|WCol-$NormsWeightCol|WNoCol-$NormsWeightNoCol|WFlTraff-$NormsWeightFluidTraffic|-Exec$NumExec.log"

	# Create parameters file
	createParamsFile "$paramsFile" "$SimMap" "$SimNewCarsFreq" "$SimNumCarsToEmit" "$SimRandomSeed" "$SimMaxTicks" "$SimUseSemaphores" "$SimOnlySemaphores" "$SimNormViolationRate" "$NormsGenEffThreshold" "$NormsGenNecThreshold" "$NormsSpecEffThreshold" "$NormsSpecNecThreshold" "$NormsUtilityWindowSize" "$NormsDefaultUtility" "$NumTicksToConverge" "$NormsWeightCol" "$NormsWeightNoCol" "$NormsWeightFluidTraffic" "$NumExec"

	# Just to give some information
	echo "|SIM|Map-$SimMap|NCarFr-$SimNewCarsFreq|NCarEm-$SimNumCarsToEmit|Seed-$SimRandomSeed|MaxTicks-$SimMaxTicks|ViolRate-$SimNormViolationRate|IRON|GenEffThr-$NormsGenEffThreshold|GenNecThr-$NormsGenNecThreshold|SpecEffThr-$NormsSpecEffThreshold|SpecNecThr-$NormsSpecNecThreshold|UWdwSz-$NormsUtilityWindowSize|DefUt-$NormsDefaultUtility|ConvTck-$NumTicksToConverve|WCol-$NormsWeightCol|WNoCol-$NormsWeightNoCol|WFlTraff-$NormsWeightFluidTraffic|-Exec$NumExec"

	# Repeat execution if errors are present...
	EXECS=0
	while [ "$EXECS" -lt "$MAX_EXECS_SAME_CONFIG_ON_ERRORS" ]; do

		$BATCH_CMD "$paramsFile" > "$logFile" 2>&1
		#$BATCH_CMD "$paramsFile"
		let EXECS++

		if [ `grep 'repast.simphony.engine.watcher.WatcheeInstrumentor' "$logFile" | wc -l` -gt 0 ]; then
			echo "repeating ($EXECS) because WatcheeInstrumentor error was present..."
		else
			EXECS=$MAX_EXECS_SAME_CONFIG_ON_ERRORS
		fi

		if [ `grep 'xception' "$logFile" | wc -l` -gt 0 ]; then
			echo "EXCEPTION on $logFile"
			echo "$logFile" >> launchExperiments.errors.txt
		fi

		if [ `grep 'ERROR' "$logFile" | wc -l` -gt 0 ]; then
			echo "ERROR on $logFile"
			echo "$logFile" >> launchExperiments.errors.txt
		fi

		if [ `grep 'WARNING' "$logFile" | wc -l` -gt 0 ]; then
			echo "WARNING on $logFile"
			echo "$logFile" >> launchExperiments.errors.txt
		fi

		mv "$logFile" "$logFile.todelete"
		grep -v "^RepastLevel" "$logFile.todelete" > "$logFile"
		rm "$logFile.todelete"
	done

	rm "$paramsFile"
}
