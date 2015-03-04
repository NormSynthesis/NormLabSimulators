#!/bin/bash

path=$1
dataFile="TrafficDataOutput.dat"

# Obtain initial variables
numFiles=`ls $path | grep $dataFile | wc -l`
firstFile=`ls $path | grep $dataFile | head -n 1`

echo `ls $path | grep $dataFile | head -n 1`

# Rename first file as the last experiment
mv "$path/$firstFile" "$path/$dataFile"".$numFiles"

# Rename the rest of the files
let numFiles=$numFiles-1
for num in `seq $numFiles -1 1`
do
	let numAux=$num+1
	file=`ls $path | grep "$dataFile.$num$"`

	if [ "$file" != "" ]
	then
		#mv "$path/$file" "$path/$dataFile"".$numAux"
		echo "$path/$file" "$path/$dataFile"".$numAux"
	fi
done

