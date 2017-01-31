Thermostat Web Server 


Setup via Docker container
==========================
==========================

This walks through the setup of Thermostat Web Server in a Docker container with authentication via Kerberos provided by a FreeIPA server running in another Docker container.

By the end of this section a Thermostat Web Server will be running within a Docker container, accessible through the container's ip address and open port 80. Users will authenticate via Kerberos with the FreeIPA server in order to successfully request from the Thermostat Web Server.


Requirements:
0. Docker
1. FreeIPA Server up and running. See the FreeIPA Server section for a guide to setting one up for testing
#TODO: Thermostat should be in the image so users don't need to mount their own
2. Thermostat image available on the system.
3. Thermostat Web Server plugin available on the system.

Build the Thermostat Web Server plugin

$ cd <path-to-thermostat-web-server>
$ mvn package

In the Thermostat image, install the Thermostat Web Server plugin by running:

$ unzip <path-to-thermostat-web-server>/distribution/target/thermostat-server-distribution-1.99.12-SNAPSHOT.zip -d <path-to-thermostat-image>/plugins

In the folder containing the Dockerfile, build the Docker image by running:

$ docker build -t thermostat-web .

Start a Docker container with:

$ docker run --name thermostat-web-container \
	-ti --privileged \
	--link freeipa-server-container:ipa.example.com \
        -v <path-to-thermostat-image>:/root/thermostat \ 
	-e PASSWORD=password \
	-h client-1.example.com \
	-e IPA_CLIENT_INSTALL_OPTS="-d --domain=example.com --server=ipa.example.com --realm=EXAMPLE.COM" \
	thermostat-web


On the FreeIPA server container, add the client service via:

$ kinit admin
$ ipa service-add HTTP/client-1.example.com --force

On the Thermostat Web Server container authenticate as the admin and run the ipa-client-setup.sh script:

$ kinit admin
$ /root/ipa-client-setup.sh client-1.example.com

#TODO: Thermostat should be in image and storage should be setup during image build
Setup and run thermostat storage:

$ /root/thermostat/bin/thermostat-devsetup
$ /root/thermostat/bin/thermostat storage --start

#TODO: Image should run these commands on startup
Start httpd and the dbus daemon:

$ httpd -k start
$ dbus-daemon --system --fork

Start the web-server:

$ /root/thermostat/bin/thermostat web-endpoint 8090

Users can authenticate via kerberos to access the endpoint at:

http://<docker-ip-address>/api

The ip address can be found with:

$ docker inspect --format '{{ .NetworkSettings.IPAddress }}' thermostat-web-container

FreeIPA Server
==============
==============

The FreeIPA Server Docker image is available at:

https://hub.docker.com/r/freeipa/freeipa-server/

The FreeIPA Server Docker image is a work in progress and may be unstable. To use a built Docker image instead of the one provided by Docker Hub, see the README.md at:

https://github.com/freeipa/freeipa-container

Stable revision: 5b041b1e01918ab2a221563c130c7bfe50546cc9


$ docker pull freeipa/freeipa-server
$ mkdir -p /var/lib/ipa-data
$ docker run --name freeipa-server-container -ti \
   -h ipa.example.com \
   -v /sys/fs/cgroup:/sys/fs/cgroup:ro \
   --tmpfs /run --tmpfs /tmp \
   -v /var/lib/ipa-data:/data:Z freeipa/freeipa-server \
   --realm=EXAMPLE.COM \
   --domain=example.com \
   --hostname=ipa.example.com \
   --ds-password=password \
   --admin-password=password \
   --unattended

Run the two commands above to start a FreeIPA Server container with host ipa.example.com

Pay attention to the admin user password as it will be needed later.



