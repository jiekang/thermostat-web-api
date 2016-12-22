#!/bin/bash

# $1 user
# $2 password
# $3 url
get() {
  curl -s -k -v -X GET --user $1:$2 "$3"
}

# $1 user
# $2 password
# $3 url
# $4 body
post() {
  curl -s -k -v -X POST --user $1:$2 -d "$4" "$3"
}

# $1 user
# $2 password
# $3 url
# $4 body
put() {
  curl -s -k -v -X PUT --user $1:$2 -d "$4" "$3"
}

check() {
  if [ "$1" == "$2" ] ; then
    echo "Pass: $3"
    exit 0;
  else
    echo "Fail: $3" 
    exit 1;
  fi 
}
