#!/bin/bash

HOSTNAME=$1

ipa service-add HTTP/${HOSTNAME}
