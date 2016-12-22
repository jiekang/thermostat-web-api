KEYTOOL=keytool
OPENSSL=openssl

all: ca.key ca.crt server.key server.csr server.crt thermostat.keystore server.keystore

ca.key:
	    $(OPENSSL) genrsa -out $@ 4096

ca.crt: ca.key
	    $(OPENSSL) req -new -x509 -days 1826 -key $< -out $@

server.key:
	    $(OPENSSL) genrsa -out $@ 4096

server.csr: server.key
	    $(OPENSSL) req -new -key $< -out $@

server.crt: server.csr ca.crt ca.key
	    $(OPENSSL) x509 -req -days 730 -in server.csr -CA ca.crt -CAkey ca.key -set_serial 01 -out $@
thermostat.keystore: ca.crt
	    $(KEYTOOL) -genkey -alias com.redhat.thermostat -keyalg RSA -keystore $@
	        $(KEYTOOL) -import -trustcacerts -alias root -file $< -keystore $@

keystore.pkcs12: server.crt server.key
	    $(OPENSSL) pkcs12 -export -in server.crt -inkey server.key -out $@

server.keystore: keystore.pkcs12
	    $(KEYTOOL) -importkeystore -srckeystore $< -destkeystore $@ -srcstoretype pkcs12

clean:
	    rm -f ca.key
	        rm -f ca.crt
		    rm -f server.key
		        rm -f server.csr
			    rm -f server.crt
			        rm -f thermostat.keystore
				    rm -f keystore.pkcs12
				        rm -f server.keystore
