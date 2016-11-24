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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

@Path("")
public class HttpHandler {

    @GET
    @Path("vm-cpu")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllVmCpuInfo() {
        if (null == MongoStorage.getDatabase()) {
            return "vm-cpu";
        }

        TimedRequest<FindIterable<Document>> request = new TimedRequest<>();
        FindIterable<Document> documents = request.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                return MongoStorage.getDatabase().getCollection("vm-cpu-stats").find().sort(new BasicDBObject("_id", -1));
            }
        });

        return DocumentResponse.build(documents, request.getElapsed());
    }

    @GET
    @Path("agents/{agentId}/vms/{vmId}/cpu")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVmCpuInfo(@PathParam("agentId") String agentId,
                                @PathParam("vmId") String vmId,
                                @QueryParam("count") @DefaultValue("1") String count,
                                @QueryParam("sort") @DefaultValue("-1") String sort,
                                @QueryParam("maxTimestamp") String maxTimestamp,
                                @QueryParam("minTimestamp") String minTimestamp) {

        if (null == MongoStorage.getDatabase()) {
            return agentId + vmId + count + sort + maxTimestamp + minTimestamp;
        }

        final int size = Integer.valueOf(count);

        Bson filter = Filters.and(Filters.eq("agentId", agentId), Filters.eq("vmId", vmId));

        if (maxTimestamp != null) {
            filter = Filters.and(Filters.lte("timeStamp", Long.valueOf(maxTimestamp)), filter);
        }
        if (minTimestamp != null) {
            filter = Filters.and(Filters.gte("timeStamp", Long.valueOf(minTimestamp)), filter);
        }

        final int sortOrder = Integer.valueOf(sort);

        TimedRequest<FindIterable<Document>> request = new TimedRequest<>();
        final Bson finalFilter = filter;
        FindIterable<Document> documents = request.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                return MongoStorage.getDatabase().getCollection("vm-cpu-stats").find(finalFilter).sort(new BasicDBObject("_id", sortOrder)).limit(size);
            }
        });

        return DocumentResponse.build(documents, request.getElapsed());
    }

    @POST
    @Path("agents/{agentId}/vms/{vmId}/cpu")
    @Produces(MediaType.APPLICATION_JSON)
    public String postVmCpuInfo(String body,
                                @PathParam("agentId") String agentId,
                                @PathParam("vmId") String vmId,
                                @QueryParam("count") @DefaultValue("1") String count,
                                @QueryParam("sort") @DefaultValue("-1") String sort) {
        if (null == MongoStorage.getDatabase()) {
            return body + agentId + vmId + count + sort;
        }
        return "POST " + agentId + " " + vmId + "\n\n" + body;
    }

    @PUT
    @Path("agents/{agentId}/vms/{vmId}/cpu")
    @Produces(MediaType.TEXT_HTML)
    public String putVmCpuInfo(String body,
                               @PathParam("agentId") String agentId,
                               @PathParam("vmId") String vmId,
                               @QueryParam("count") @DefaultValue("1") String count,
                               @QueryParam("sort") @DefaultValue("-1") String sort) {
        if (null == MongoStorage.getDatabase()) {
            return body + agentId + vmId + count + sort;
        }
        return "PUT " + agentId + " " + vmId + "\n\n" + body;
    }
}