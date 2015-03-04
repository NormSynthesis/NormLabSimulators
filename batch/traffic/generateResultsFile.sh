#!/bin/bash

path=$1
totalNormsFile=$2
finalNormsFile=$3
outputFile=$1/$4

SimMap=$5
SimNewCarsFreq=$6
SimNumCarsToEmit=$7
SimVariableSpeed=$8
SimRandomSeed=$9
SimMaxTicks=${10}
SimUseSemaphores=${11}
SimOnlySemaphores=${12}

SolsOnlyCollided=${13}

NormsUseSpeed=${14}
NormsIncOwnState=${15}
NormsExclude=${16}
NormsViolatingProb=${17}
NormsMinExperience=${18}
NormsMaxCyclesNoApply=${19}
NormsScoreWindowSize=${20}
NormsMinScore=${21}
NormsWeightCol=${22}
NormsWeightNoCol=${23}
NormsWeightFluidTraffic=${24}

echo "Generating results file..."

echo -n "\documentclass[a4paper,10pt]{article}

\usepackage[utf8x]{inputenc}
\usepackage{graphicx}
\usepackage{fancyvrb}
\usepackage[margin=2.5cm,nohead]{geometry}
\DefineVerbatimEnvironment{code}{Verbatim}{fontsize=\small}
\DefineVerbatimEnvironment{example}{Verbatim}{fontsize=\small}

\title{Results for experiments in $path }
\author{}
\date{\today}

\begin{document}
\maketitle
" > $outputFile

# Configuration section
if [ $# -gt 1 ]
then
  echo -n "
\section{Configuration}

Configuration for all the executions in $path

\subsection{Simulator}
\begin{itemize}
  \item Map: $SimMap
  \item New cars frequency (every N ticks): $SimNewCarsFreq
  \item Num cars to emit at the same time: $SimNumCarsToEmit
  \item variable speed: $SimVariableSpeed
  \item Random seed: $SimRandomSeed
  \item Max ticks: $SimMaxTicks
  \item Use semaphores: $SimUseSemaphores
  \item Only semaphores: $SimOnlySemaphores
\end{itemize}

\subsection{Solutions}
\begin{itemize}
  \item Generate solutions only for collided: $SolsOnlyCollided
\end{itemize}

\subsection{Norms}
\begin{itemize}
  \item Use speed in their scopes: $NormsUseSpeed
  \item Include own state of the car: $NormsIncOwnState
  \item Norms excluding: $NormsExclude
  \item Violating probability: $NormsViolatingProb
  \item Minimum experience to evaluate a norm (in number of activations): $NormsMinExperience
  \item Max cycles without being applied a norm to set it as inactive: $NormsMaxCyclesNoApply
  \item Score window size: $NormsScoreWindowSize
  \item Minimum score to set the norm as inactive: $NormsMinScore " >> $outputFile

  echo '  \item $W_{col}$: ' "$NormsWeightCol" >> $outputFile  
  echo '  \item $W_{noCol}$: ' "$NormsWeightNoCol" >> $outputFile  
  echo '  \item $W_{fluidTraffic}$: ' "$NormsWeightFluidTraffic" >> $outputFile  
  echo "\end{itemize}" >> $outputFile
fi

# Final norms section
echo -n "
\newpage 

\section{Final norms generated}

Next we show the list of the final norms generated in the N executions,
with the number of appearances of each one of them." >> $outputFile

i=0
cat $path/$finalNormsFile | while read line; do

  # Treat new norm
  if [ "$line" = "N" ]
  then 

    # Close the last norm if we are not in the first one
    if [ $i -gt 0 ]; then 
      echo "\end{code}" >> $outputFile
    fi
    let i=$i+1

    # Write new norm
    echo "" >> $outputFile
    echo "\subsection{Norm N$i }" >> $outputFile
    echo "" >> $outputFile
    echo "\begin{code}" >> $outputFile
    echo "Scope of the norm:" >> $outputFile

  # Number of appearances of the norm
  elif [ "${line:0:1}" = "A" ] 
  then
    echo "Number of appearances: "${line:1}"" >> $outputFile

  # Scope of the norm
  else
    echo "$line" >> $outputFile
  fi  
done

echo "\end{code}" >> $outputFile

echo -n "
\begin{figure}
\includegraphics[scale=0.55]{$path/results/plot-averages.png}
\caption{Average results of all the simulations}
\end{figure}

\begin{figure}
\includegraphics[scale=0.55]{$path/results/plot-TrafficNorms-Final.png}
\caption{Total traffic norms appeared in all the simulations}
\end{figure}
" >> $outputFile

# Total norms section
echo -n "
\newpage 

\section{Total norms generated}

Next we show the list of the total norms generated in the N executions,
with the number of appearances of each one of them." >> $outputFile

i=0
cat $path/$totalNormsFile | while read line; do

  # Treat new norm
  if [ "$line" = "N" ]
  then 

    # Close the last norm if we are not in the first one
    if [ $i -gt 0 ]; then 
      echo "\end{code}" >> $outputFile
    fi
    let i=$i+1

    # Write new norm
    echo "" >> $outputFile
    echo "\subsection{Norm N$i }" >> $outputFile
    echo "" >> $outputFile
    echo "\begin{code}" >> $outputFile
    echo "Scope of the norm:" >> $outputFile

  # Number of appearances of the norm
  elif [ "${line:0:1}" = "A" ] 
  then
    echo "Number of appearances: "${line:1}"" >> $outputFile

  # Scope of the norm
  else
    echo "$line" >> $outputFile
  fi  
done

echo "\end{code}" >> $outputFile

echo -n "
\begin{figure}
\includegraphics[scale=0.55]{$path/results/plot-TrafficNorms-Total.png}
\caption{Final traffic norms appeared in all the simulations}
\end{figure}

" >> $outputFile

echo "\end{document}" >> $outputFile

