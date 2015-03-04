FILEDIR=$1
FILENAME=$2

YRANGE="[0:100]"
TITLE="Statistics"
XLABEL="Ticks"

echo "Plotting data for file $FILEDIR/$FILENAME..."

# Generate gnuplot file to load it
echo -n "

reset

set output 'plot-$FILENAME.png'
set terminal png nocrop enhanced font FreeSans 10 size 840,640
set title '$TITLE' font 'FreeSerif,12'
set xlabel '$XLABEL'
set key right top  

plot       '$FILEDIR/$FILENAME' using 1:3 w lines title 'AvgNoViolCollisions' lt 3 lw 1 lc rgb 'blue'"     >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:4 w lines title 'NumberOfCases' lt 3 lw 1 lc rgb 'green'"    >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:5 w lines title 'NumberOfSolutions' lt 3 lw 1 lc rgb 'orange'"   >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:6 w lines title 'NumberOfNorms' lt 3 lw 1 lc rgb 'black'"    >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:7 w lines title 'NumberOfActiveNorms' lt 3 lw 1 lc rgb 'red'"    >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:9 w lines title 'CarsThroughput' lt 3 lw 1 lc rgb 'green'"    >> "$FILEDIR/$FILENAME.gnuplot"
echo -n ", '$FILEDIR/$FILENAME' using 1:10 w lines title 'NumberOfStops' lt 3 lw 1 lc rgb 'pink'"    >> "$FILEDIR/$FILENAME.gnuplot"

# Generate separate gnuplot file for total collisions
echo -n "

reset

set output 'plot-$FILENAME.cols.png'
set terminal png nocrop enhanced font FreeSans 10 size 840,640
set title '$TITLE' font 'FreeSerif,12'
set xlabel '$XLABEL'
set key right top  

plot       '$FILEDIR/$FILENAME' using 1:2 w lines title 'AvgCollisions' lt 3 lw 1 lc rgb 'red'"      >  "$FILEDIR/$FILENAME.cols.gnuplot"

# Generate histogram for final norms
echo -n "

reset

set output 'plot-TrafficNorms-Final.png'
set terminal png nocrop enhanced font FreeSans 10 size 840,640
set title '$TITLE' font 'FreeSerif,12'
set xlabel '$XLABEL'
set key right top  
set auto x
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set xtic rotate by -45 scale 0
plot '$FILEDIR/TrafficNormsFinal.dat.plot' using 2:xtic(1) ti col  ">  "$FILEDIR/TrafficNormsFinal.gnuplot"

# Generate histogram for total norms
echo -n "

reset

set output 'plot-TrafficNorms-Total.png'
set terminal png nocrop enhanced font FreeSans 10 size 840,640
set title '$TITLE' font 'FreeSerif,12'
set xlabel '$XLABEL'
set key right top  
set auto x
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set xtic rotate by -45 scale 0
plot '$FILEDIR/TrafficNormsTotal.dat.plot' using 2:xtic(1) ti col  ">  "$FILEDIR/TrafficNormsTotal.gnuplot"

# Generate histogram for final norm sets
echo -n "

reset

set output 'plot-TrafficNormSets-Final.png'
set terminal png nocrop enhanced font FreeSans 10 size 840,640
set title '$TITLE' font 'FreeSerif,12'
set xlabel '$XLABEL'
set key right top  
set auto x
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set xtic rotate by -45 scale 0
plot '$FILEDIR/TrafficNormSetsFinal.dat.plot' using 2:xtic(1) ti col  ">  "$FILEDIR/TrafficNormSetsFinal.gnuplot"

# Plot
export GDFONTPATH=/usr/share/fonts/truetype/freefont/
pushd . 1> /dev/null
cd "$FILEDIR"
gnuplot "$FILEDIR/$FILENAME.gnuplot"
gnuplot "$FILEDIR/$FILENAME.cols.gnuplot"
gnuplot "$FILEDIR/TrafficNormsFinal.gnuplot"
gnuplot "$FILEDIR/TrafficNormsTotal.gnuplot"
gnuplot "$FILEDIR/TrafficNormSetsFinal.gnuplot"
popd    1> /dev/null

