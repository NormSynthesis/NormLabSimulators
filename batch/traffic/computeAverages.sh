#!/bin/bash

# ----------------------------------------------------------
# Computes the averages of the columns from different files
#
# Example: 
#   File 1    File 2
#   1 4 6 7   0 5 3 2
#
# Result:
#   1 9 9 9 -> (0+1) (4+5) (6+3) (7+2)
#
# Parameters: Receives a directory and computes the average 
#	      of all the files in the directory
#             (WARNING: All the files in the directory must
#             have the same format. Else, will not work.
#
# Returns: A file 'averages.dat' in that directory
# ----------------------------------------------------------

source `dirname $0`/experimentSettings.sh

dirToCompute=$1

# Obtain initial variables
numExps=`ls $dirToCompute | grep $dataFile | wc -l`
firstFile=`ls $dirToCompute | grep $dataFile | head -n 1`
numCols=`cat $dirToCompute/$firstFile | awk 'BEGIN {} ; END{print NF}'`

echo ""

# Remove previous average.dat file if needed
if [ `ls $dirToCompute | grep averages.dat | wc -l` -gt 0 ]; then
	echo "Removing previous averages.dat file..."
	rm $dirToCompute/averages.dat
fi

echo "Computing averages..."

# Continue with the average computation
if [ "$numExps" = "1" ]; then
	mv "$dirToCompute/$dataFile.1" $dirToCompute/averages.dat
else
	# Call the awk function that computes the averages
	paste -d " " $dirToCompute/$dataFile* | nawk -v numExps="$numExps" -v numCols="$numCols" -v dirToCompute=$dirToCompute '{

		# <- For each line of the entry

		# For the first line simply write the header
		if(NR==1) {
			line = ""
			for(i=0; i<(numCols); i++) {
				if(i==0)	line = line $(i+1)
				else		line = line " " $(i+1)
			}
			print line >> dirToCompute"/averages.dat"

		# The rest of the lines
		} else {

			# Initialize value array
			for(i=0; i<(numCols); i++) {
				colValues[i] = 0;
			}

			# For each column of the generated entry (with paste -d) we sum
			for(i=0; i<(numCols*numExps); i++)
			{
				col=i%numCols

				# The tick field (column 1) does not need to be averaged
				if(col == 0) {
					colValues[col] = $(i+1)

				# For the other columns, we compute their average
				} else {
					colValues[col] = colValues[col] + $(i+1)
				}
			}

			# Divide to compute the average
			for(i=0; i<(numCols); i++) {
				if(i!=0) {
					colValues[i] = colValues[i] / numExps;
				}
			}

			# Write to a new file that contains the averages
			line = ""
			for(i=0; i<(numCols); i++) {
				if(i==0)	line = line colValues[i]
				else		line = line " " colValues[i]
			}
			print line >> dirToCompute"/averages.dat"
		}
	}'
fi

echo "File averages.dat generated"
