#!/bin/bash

SOURCE_DIR=$(dirname $(readlink -f $0))
source ${SOURCE_DIR}/base.sh

BASE="http://localhost:8090/agents/1?user=blob"

OUT=$(get ${BASE})
echo ${OUT}

OUT=$(put ${BASE} "hello")
echo ${OUT}