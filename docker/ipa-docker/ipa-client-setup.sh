#!/bin/bash

HOSTNAME=$1

ipa-getkeytab -s $( awk '/^server/ { print $3 }' /etc/ipa/default.conf ) -k /etc/http.keytab -p HTTP/${HOSTNAME}
chown apache /etc/http.keytab
chmod 600 /etc/http.keytab
