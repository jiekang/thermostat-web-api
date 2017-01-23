/*
 * Copyright 2012-2016 Red Hat, Inc.
 *
 * This file is part of Thermostat.
 *
 * Thermostat is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * Thermostat is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Thermostat; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */

package com.redhat.thermostat.web2.endpoint.command;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.UriBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.redhat.thermostat.common.cli.AbstractCommand;
import com.redhat.thermostat.common.cli.Command;
import com.redhat.thermostat.common.cli.CommandContext;
import com.redhat.thermostat.common.cli.CommandException;
import com.redhat.thermostat.shared.config.CommonPaths;
import com.redhat.thermostat.web2.endpoint.security.auth.basic.BasicAuthFilter;
import com.redhat.thermostat.web2.endpoint.security.auth.proxy.ProxyAuthFilter;
import com.redhat.thermostat.web2.endpoint.web.handler.http.HttpHandler;
import com.redhat.thermostat.web2.endpoint.web.handler.storage.MongoStorageHandler;

@Component
@Service(Command.class)
@Property(name=Command.NAME, value="web-endpoint")
public class WebEndpointCommand extends AbstractCommand {

    private Server server;

    @Reference
    private CommonPaths commonPaths;

    @Override
    public void run(CommandContext ctx) throws CommandException {
        int port = Integer.valueOf(ctx.getArguments().getNonOptionArguments().get(0));

        MongoStorage.start("thermostat", 27518);

        startServer(port);
    }

    private void startServer(int port) {
        URI baseUri = UriBuilder.fromUri("http://localhost").port(8080).build();
        ResourceConfig config = new ResourceConfig();
        config.register(new HttpHandler(new MongoStorageHandler()));
        config.register(new ProxyAuthFilter());
        config.register(new RolesAllowedDynamicFeature());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("");
        URL u = this.getClass().getResource("/swagger/index.html");
        URI root;
        try {
            root = u.toURI().resolve("./").normalize();
            resourceHandler.setBaseResource(Resource.newResource(root));
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        config.register(resourceHandler);

        server = JettyHttpContainerFactory.createServer(baseUri, config, false);

        HttpConfiguration httpConfig = new HttpConfiguration();
//        httpConfig.setSecureScheme("https");
//        httpConfig.setSecurePort(8443);
//        httpConfig.setOutputBufferSize(32768);
//        httpConfig.setRequestHeaderSize(8192);
//        httpConfig.setResponseHeaderSize(8192);
//        httpConfig.setSendServerVersion(true);
//        httpConfig.setSendDateHeader(false);

        SslContextFactory sslContextFactory = new SslContextFactory();

        sslContextFactory.setKeyStorePath(commonPaths.getUserThermostatHome().getAbsolutePath() + "/server.keystore");
        sslContextFactory.setKeyStorePassword("password");

        // SSL HTTP Configuration
        HttpConfiguration sslConfig = new HttpConfiguration(httpConfig);
        sslConfig.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(sslConfig));
        sslConnector.setHost("localhost");
        sslConnector.setPort(port + 1);
        sslConnector.setIdleTimeout(30000);


        server.setConnectors(new Connector[]{sslConnector});


        Handler orig = server.getHandler();
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, orig});
        server.setHandler(handlers);

        try {
            server.start();
            System.out.println(server.dump());
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MongoStorage.finish();
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

    @Override
    public boolean isStorageRequired() {
        return false;
    }
}