#!/bin/bash

SOURCE_DIR=$(dirname $(readlink -f $0))
source ${SOURCE_DIR}/base.sh

BASE="http://localhost:8090/agents?user=$1"

BODY="{\"agentId\":\"abc\"}"
OUT=$(put ${BASE} "{\"agentId\":\"$2\"}")
echo ${OUT}
