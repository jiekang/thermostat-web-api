FROM freeipa-f23-client

RUN dnf install -y httpd maven mongodb mongodb-server mercurial sssd-dbus mod_auth_gssapi mod_lookup_identity && dnf clean all

COPY kerberos.conf /etc/httpd/conf.d/kerberos.conf
COPY thermostat.conf /etc/httpd/conf.d/thermostat.conf
COPY ipa-client-setup.sh /root/ipa-client-setup.sh

RUN mkdir -p /root/data/db && touch /root/data/mongod.log
RUN mkdir -p /run/dbus && touch /run/dbus/system_bus_socket
