#!/bin/bash

SOURCE_DIR=$(dirname $(readlink -f $0))
source ${SOURCE_DIR}/base.sh

FAIL=0

BASE="http://localhost:8090/agents/1/vms/1/cpu"
QUERY="${BASE}?count=5&sort=1&maxTimestamp=2&minTimestamp=1"
QUERY2="${BASE}?count=4&sort=1"
BODY="hello"

OUT=$(get ${BASE})
if [ "111-1nullnull" == "${OUT}" ] ; then
  echo "Pass : GET ${BASE}"
else
  echo "Fail : GET ${BASE}"
  FAIL=1
fi

OUT=$(get ${QUERY})
if [ "115121" == "${OUT}" ] ; then
  echo "Pass : GET ${QUERY}"
else
  echo "Fail : GET ${QUERY}"
  FAIL=1
fi

OUT=$(post ${BASE})
if [ "111-1" == "${OUT}" ] ; then
  echo "Pass : POST ${BASE}"
else
  echo "Fail : POST ${BASE}"
  FAIL=1
fi

OUT=$(post ${QUERY2} ${BODY})
if [ "${BODY}1141" == "${OUT}" ] ; then
  echo "Pass : POST ${QUERY2} with body ${BODY}"
else
  echo "Fail : POST ${QUERY2} with body ${BODY}"
  FAIL=1
fi

OUT=$(put ${BASE})
if [ "111-1" == "${OUT}" ] ; then
  echo "Pass : PUT ${BASE}"
else
  echo "Fail : PUT ${BASE}"
  FAIL=1
fi

OUT=$(put ${QUERY2} ${BODY})
if [ "${BODY}1141" == "${OUT}" ] ; then
  echo "Pass : POST ${QUERY2} with body ${BODY}"
else
  echo "Fail : POST ${QUERY2} with body ${BODY}"
  FAIL=1
fi



if [ "1" -eq "$FAIL" ] ; then
  exit 1
fi 

exit 0
