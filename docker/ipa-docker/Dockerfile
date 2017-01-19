FROM freeipa-f23-client

RUN dnf install -y maven mongodb mongodb-server mercurial sssd-dbus mod_auth_gssapi mod_lookup_identity && dnf clean all

COPY mongo-setup.js /root/mongo-setup.js
COPY mongo.sh /root/mongo.sh

RUN mkdir -p /root/data/db && touch /root/data/mongod.log
