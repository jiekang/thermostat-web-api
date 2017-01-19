#!/bin/bash

docker run --name tms-ipa-web-$1 \
	-ti --privileged \
	--link freeipa-server-container-1:ipa.example.com \
	-e PASSWORD=password \
	-v /home/jkang/work/thermostat/thermostat-web-api/tms-web-endpoint:/root/tms-web-endpoint \
	-h client-$1.example.com \
	-e IPA_CLIENT_INSTALL_OPTS="-d --domain=example.com --server=ipa.example.com --realm=EXAMPLE.COM" \
	tms-ipa-web
