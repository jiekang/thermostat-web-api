#!/bin/bash

SOURCE_DIR=$(dirname $(readlink -f $0))
source ${SOURCE_DIR}/base.sh

BASE="https://localhost:9091/api/agents/all"

BODY="{\"agentId\":\"abc\"}"
OUT=$(put $1 $1 ${BASE} "{\"agentId\":\"$2\"}")
echo ${OUT}
