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

package com.redhat.thermostat.web2.endpoint.web.handler.http;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.redhat.thermostat.web2.endpoint.web.handler.storage.StorageHandler;

@Path("")
@RolesAllowed("user")
public class HttpHandler {

    private StorageHandler handler;

    public HttpHandler(StorageHandler handler) {
        this.handler = handler;
    }

    @GET
    @Path("agents/{agentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAgent(@Context SecurityContext securityContext,
                             @PathParam("agentId") String agentId,
                             @QueryParam("size") @DefaultValue("1") String count,
                             @QueryParam("sort") @DefaultValue("-1") String sort,
                             @QueryParam("cursor") @DefaultValue("-1") String cursor) {
        return handler.getAgent(securityContext, agentId, count, sort, cursor);
    }

    @PUT
    @Path("agents/{agentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putAgent(String body,
                             @Context SecurityContext context) {
        return handler.putAgent(body, context);
    }

    @GET
    @Path("agents/{agentId}/host/cpu")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVmCpuInfo(@Context SecurityContext securityContext,
                               @PathParam("agentId") String agentId,
                               @QueryParam("size") @DefaultValue("1") String count,
                               @QueryParam("sort") @DefaultValue("-1") String sort,
                               @QueryParam("maxTimestamp") String maxTimestamp,
                               @QueryParam("minTimestamp") String minTimestamp) {
        return handler.getVmCpuInfo(securityContext, agentId, count, sort, maxTimestamp, minTimestamp);
    }
}