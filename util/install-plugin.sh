#!/bin/bash

#
# Installs this project as a plugin
#

PLUGIN_NAME=${PWD##*/}
PLUGIN_VERSION=1.99.12-SNAPSHOT
PLUGIN_ZIP=thermostat-${PLUGIN_NAME}-distribution

THERMOSTAT_HOME=INSERT-PATH-TO-THERMOSTAT-REPOSITORY-HERE
THERMOSTAT_IMAGE=${THERMOSTAT_HOME}/distribution/target/image
PLUGIN_HOME="${THERMOSTAT_HOME}"/distribution/target/image/plugins

rm -rfv ${PLUGIN_HOME}/${PLUGIN_NAME} 
unzip -o ./distribution/target/${PLUGIN_ZIP}-${PLUGIN_VERSION}.zip -d ${THERMOSTAT_IMAGE}
