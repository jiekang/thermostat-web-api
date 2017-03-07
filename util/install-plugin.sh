#!/bin/bash

#
# Installs this project as a plugin
#

PLUGIN_NAME=${PWD##*/}
PLUGIN_VERSION=1.99.12-SNAPSHOT
PLUGIN_ZIP=thermostat-${PLUGIN_NAME}-distribution

if [ -z ${THERMOSTAT_HOME} ]; then
  echo 1>&2 "THERMOSTAT_HOME environment variable not set. Required for installation. It should point to your Thermostat installation."
  exit 1
fi
PLUGIN_HOME="${THERMOSTAT_HOME}/plugins"

rm -rfv ${PLUGIN_HOME}/${PLUGIN_NAME} 
unzip -o ./distribution/target/${PLUGIN_ZIP}-${PLUGIN_VERSION}.zip -d ${THERMOSTAT_HOME}
