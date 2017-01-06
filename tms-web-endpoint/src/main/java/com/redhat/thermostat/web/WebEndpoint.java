package com.redhat.thermostat.web;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.redhat.thermostat.security.ProxyFilter;
import com.redhat.thermostat.web.handler.http.HttpHandler;
import com.redhat.thermostat.web.handler.storage.HibernateHandler;

public class WebEndpoint {

    private final String host;
    private final int port;
    private Server server;

    public WebEndpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        URI baseUri = UriBuilder.fromUri("http://localhost").port(8080).build();
        ResourceConfig config = new ResourceConfig();
        config.register(new HttpHandler(new HibernateHandler()));
        config.register(new ProxyFilter());
        config.register(new RolesAllowedDynamicFeature());

        server = JettyHttpContainerFactory.createServer(baseUri, config, false);

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.addCustomizer(new org.eclipse.jetty.server.ForwardedRequestCustomizer());


        ServerConnector httpConnector = new ServerConnector(server,
                new HttpConnectionFactory(httpConfig));
        httpConnector.setHost(host);
        httpConnector.setPort(port);
        httpConnector.setIdleTimeout(30000);


        server.setConnectors(new Connector[]{httpConnector});

        try {
            server.start();
            System.out.println(server.dump());
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    private void stop() {
        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
