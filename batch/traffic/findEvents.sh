#!/bin/bash

path=$1
fileType=$2
event=$3
minTick=$4

files=`ls $path/*.$fileType`
outFile="EVENT_""$event""_Matches.log"

echo "Searching for the event $event in $path..."

# For each file we search into it for the required event
for file in $files; do
	echo " - Searching into $file..."

	#eventMatches=`cat $file | grep "EVENT: $event"`

	i=0
	grep "EVENT: $event " $file |
	while read line; do
		tick=`echo $line | egrep " [0-9]*{1,}" -o`

		if [ $tick -ge $minTick ]; then
			let i=$i+1
			if [ $i = 1 ]; then 
				echo "* MATCH in File $file" >> $outFile; echo ""
			fi
			
			echo "   - $line" >> $outFile
		fi
	done
done


if [ `ls | grep $outFile | wc -l` -gt "0" ]; then
	echo "File $outFile generated"
fi

