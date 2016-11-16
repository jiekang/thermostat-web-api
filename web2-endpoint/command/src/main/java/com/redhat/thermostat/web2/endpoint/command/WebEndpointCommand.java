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

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.redhat.thermostat.common.cli.AbstractCommand;
import com.redhat.thermostat.common.cli.Command;
import com.redhat.thermostat.common.cli.CommandContext;
import com.redhat.thermostat.common.cli.CommandException;

@Component
@Service(Command.class)
@Property(name=Command.NAME, value="web-endpoint")
public class WebEndpointCommand extends AbstractCommand {

    private Server server;

    @Override
    public void run(CommandContext ctx) throws CommandException {
        startServer();
    }

    private void startServer() {
        URI baseUri = UriBuilder.fromUri("http://localhost").port(8080).build();
        ResourceConfig config = new ResourceConfig();
        config.register(new HttpHandler());

        server = JettyHttpContainerFactory.createServer(baseUri, config, false);

        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setHost("localhost");
        httpConnector.setIdleTimeout(30000);
        httpConnector.setPort(8082);

        server.setConnectors(new Connector[]{httpConnector});

        try {
            server.start();
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

    @Override
    public boolean isStorageRequired() {
        return false;
    }
}