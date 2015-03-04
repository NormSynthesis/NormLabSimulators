#!/bin/bash

IN_PARAMS=$1

echo "IN_PARAMS($IN_PARAMS)"

source `dirname $0`/config.sh

BATCH_PARAMS="-params $IN_PARAMS"

pushd .
cd $SIM_DIR

CLASSPATH=$CLASSPATH $JAVA_EXECUTABLE $JAVA_FLAGS -Xss10M -Xmx400M repast.simphony.batch.BatchMain $BATCH_PARAMS  $SIM_DIR/repast-settings/TrafficJunction.rs 

popd
