#!/bin/bash

HOSTNAME=$1

ipa-getkeytab -s $( awk '/^server/ { print $3 }' /etc/ipa/default.conf ) -k /etc/http.keytab -p HTTP/${HOSTNAME}
chown apache /etc/http.keytab
chmod 600 /etc/http.keytab

sed -i 's|[ifp]|[ifp]\nallowed_uids = apache, root|g' /etc/sssd/sssd.conf
sed -i 's|services = nss, sudo, pam|services = nss, sudo, pam, ifp|g' /etc/sssd/sssd.conf
