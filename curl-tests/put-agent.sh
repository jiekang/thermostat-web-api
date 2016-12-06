#!/bin/bash

SOURCE_DIR=$(dirname $(readlink -f $0))
source ${SOURCE_DIR}/base.sh

BASE="http://localhost:8090/agents"

BODY="{\"agentId\":\"abc\"}"
OUT=$(put $1 $1 ${BASE} "{\"agentId\":\"$2\"}")
echo ${OUT}
