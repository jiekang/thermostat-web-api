#!/bin/bash

THERMOSTAT_IMAGE=/home/jkang/work/thermostat/tms-work/distribution/target/image

docker run --name tms-ipa-web-$1 \
	-ti --privileged \
	--link freeipa-server-container-1:ipa.example.com \
	-e PASSWORD=password \
	-v ${THERMOSTAT_IMAGE}:/root/thermostat \
	-h client-$1.example.com \
	-e IPA_CLIENT_INSTALL_OPTS="-d --domain=example.com --server=ipa.example.com --realm=EXAMPLE.COM --force-join" \
	tms-ipa-web
