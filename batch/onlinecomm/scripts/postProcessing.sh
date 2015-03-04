#!/bin/bash

#_______________________________________________________________
#
#
#      POST PROCESSING FOR SIMULATOR EXPERIMENTS OUTPUTS.
#
#           + Draw plots of each run.
#           + Average files (.dat) creations of the combination of each run.
#           + Draw plots of average files.
#
#
#
#      Call:
#
#             bash postProcessing {PATH}
#
#      Info:
#           numer of runs of 10 (hardcoded)
#
#
#_______________________________________________________________

path="$1"

actualPath=`pwd`

cd $path

folders=`ls -d */`

cd $actualPath

for folder in $folders; do
    bash plot $path$folder                          # Draw plots for each run.
    python createAverage.py $path$folder 10         # Compute averages and draw plots.
    python createResultXML.py $path$folder          # Compute a resuming xml of experiments.
done
