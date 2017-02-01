package com.redhat.thermostat.web2.endpoint.web.handler.storage;

import java.io.IOException;
import java.util.Collections;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.glassfish.jersey.server.ChunkedOutput;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.redhat.thermostat.web2.endpoint.command.MongoStorage;
import com.redhat.thermostat.web2.endpoint.web.filters.RequestFilters;
import com.redhat.thermostat.web2.endpoint.web.json.DocumentBuilder;
import com.redhat.thermostat.web2.endpoint.web.request.TimedRequest;
import com.redhat.thermostat.web2.endpoint.web.response.MongoResponseBuilder;

public class MongoStorageHandler implements StorageHandler {

    private final int MAX_MONGO_DOCUMENTS = 5000;

    @Override
    public void getAgent(final SecurityContext securityContext,
                         final AsyncResponse asyncResponse,
                         final String agentId,
                         final String count,
                         final String sort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!MongoStorage.isConnected()) {
                    asyncResponse.resume(Response.status(Response.Status.OK).entity("GET " + agentId + " " + count + " " + sort + " " + securityContext.getUserPrincipal().getName()).build());
                    return;
                }

                final int limit = Math.min(Integer.valueOf(count), MAX_MONGO_DOCUMENTS);
                final int sortOrder = Integer.valueOf(sort);
                final String userName = securityContext.getUserPrincipal().getName();
                final Bson filter = RequestFilters.buildGetFilter(agentId, Collections.singletonList(userName));

                TimedRequest<FindIterable<Document>> timedRequest = new TimedRequest<>();

                FindIterable<Document> documents = timedRequest.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
                    @Override
                    public FindIterable<Document> run() {
                        return MongoStorage.getDatabase().getCollection("agents").find(filter).sort(new BasicDBObject("_id", sortOrder)).limit(limit);
                    }
                });

                asyncResponse.resume(Response.status(Response.Status.OK).entity(MongoResponseBuilder.buildJsonResponse(documents, timedRequest.getElapsed())).build());
            }
        }).start();
    }

    @Override
    public Response putAgent(String body,
                             @Context SecurityContext context) {
        if (!MongoStorage.isConnected()) {
            return Response.status(Response.Status.OK).entity("PUT " + context.getUserPrincipal().getName() + "\n\n" + body).build();
        }

        TimedRequest<FindIterable<Document>> timedRequest = new TimedRequest<>();

        /*
         * TODO: Verify body matches expected schema
         * TODO: Clean up insertion of tags into JSON body
         */
        final Document item = Document.parse(DocumentBuilder.addTags(body, context.getUserPrincipal().getName()));

        timedRequest.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                MongoStorage.getDatabase().getCollection("agents").insertOne(item);
                return null;
            }
        });

        return Response.status(Response.Status.OK).entity("PUT successful").build();
    }

    @Override
    public Response getHostCpuInfo(SecurityContext securityContext,
                                   String agentId,
                                   String count,
                                   String sort,
                                   String maxTimestamp,
                                   String minTimestamp) {
        if (!MongoStorage.isConnected()) {
            return Response.status(Response.Status.OK).entity(agentId + count + sort + maxTimestamp + minTimestamp).build();
        }

        final int size = Integer.valueOf(count);

        final String userName = securityContext.getUserPrincipal().getName();
        final Bson filter = RequestFilters.buildGetFilter(agentId, Collections.singletonList(userName), maxTimestamp, minTimestamp);

        final int sortOrder = Integer.valueOf(sort);

        TimedRequest<FindIterable<Document>> request = new TimedRequest<>();
        FindIterable<Document> documents = request.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                return MongoStorage.getDatabase().getCollection("cpu-stats").find(filter).sort(new BasicDBObject("_id", sortOrder)).limit(size);
            }
        });

        return Response.status(Response.Status.OK).entity(MongoResponseBuilder.buildJsonResponse(documents, request.getElapsed())).build();
    }

    @Override
    public ChunkedOutput<String> streamHostCpuInfo(SecurityContext securityContext, String agentId) {
        final ChunkedOutput<String> output = new ChunkedOutput<>(String.class, "\r\n");

        new Thread() {
            public void run() {
                try {

                    while (true) {
                        TimedRequest<FindIterable<Document>> request = new TimedRequest<>();
                        FindIterable<Document> documents = request.run(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
                            @Override
                            public FindIterable<Document> run() {
                                return MongoStorage.getDatabase().getCollection("cpu-stats").find().sort(new BasicDBObject("_id", -1)).limit(1);
                            }
                        });
                        output.write(MongoResponseBuilder.buildJsonResponse(documents, request.getElapsed()));

                        Thread.sleep(1000L);
                    }
                } catch (IOException | InterruptedException e) {
                    // An IOException occurs when reader closes the connection
                } finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return output;
    }
}
