#!/bin/bash

# $1 url
get() {
  curl -s -X GET "$1"
}

# $1 url
# $2 body
post() {
  curl -s -X POST -d "$2" "$1"
}

# $1 url
# $2 body
put() {
  curl -s -X PUT -d "$2" "$1"
}
