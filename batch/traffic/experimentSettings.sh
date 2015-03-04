#!/bin/bash

#------------------------------------------
# Settings for the experiments
#------------------------------------------

outputDataFile="TrafficDataOutput.dat"
totalNormsFile="TrafficNormsTotal.dat"
finalNormsFile="TrafficNormsFinal.dat"
resultsFile="Results"
resultsDir="results"

InitialNumExec=1
NumExecsForExperiment=200
UseSequentialRandomSeeds=false

#TrafficSim
SimMap=1
SimNewCarsFreq=1
SimNumCarsToEmit=4
SimRandomSeed=0l
SimMaxTicks=35000l
SimUseSemaphores=false
SimOnlySemaphores=false
SimNormViolationRate=0.3

#IRON
NormsGenEffThreshold=0.0
NormsGenNecThreshold=0.0
NormsSpecEffThreshold=0.026
NormsSpecNecThreshold=0.026
NormsUtilityWindowSize=150
NormsDefaultUtility=0.5
NumTicksToConverge=5000l
NormsWeightCol=4
NormsWeightNoCol=1
NormsWeightFluidTraffic=2
