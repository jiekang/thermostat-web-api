package com.redhat.thermostat.web2.endpoint.web.handler.storage;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.glassfish.jersey.server.ChunkedOutput;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.redhat.thermostat.web2.endpoint.command.MongoStorage;
import com.redhat.thermostat.web2.endpoint.web.cursor.CursorId;
import com.redhat.thermostat.web2.endpoint.web.cursor.MongoCursor;
import com.redhat.thermostat.web2.endpoint.web.cursor.CursorStore;
import com.redhat.thermostat.web2.endpoint.web.filters.RequestFilters;
import com.redhat.thermostat.web2.endpoint.web.json.DocumentBuilder;
import com.redhat.thermostat.web2.endpoint.web.request.TimedRequest;
import com.redhat.thermostat.web2.endpoint.web.response.MongoResponseBuilder;

public class MongoStorageHandler implements StorageHandler {

    private final int MAX_MONGO_DOCUMENTS = 5000;

    @Override
    public Response getAgent(@Context SecurityContext securityContext,
                             @PathParam("agentId") String agentId,
                             @QueryParam("size") @DefaultValue("1") String count,
                             @QueryParam("sort") @DefaultValue("-1") String sort,
                             @QueryParam("cursor") @DefaultValue("-1") String cursor) {
        if (!MongoStorage.isConnected()) {
            return Response.status(Response.Status.OK).entity("GET " + agentId + " " + count + " " + sort + " " + securityContext.getUserPrincipal().getName()).build();
        }

        final int limit = Math.min(Integer.valueOf(count), MAX_MONGO_DOCUMENTS);
        final int sortOrder = Integer.valueOf(sort);
        final String userName = securityContext.getUserPrincipal().getName();
        final Bson filter = RequestFilters.buildGetFilter(agentId, Arrays.asList(userName));

        final StringBuilder prevCursor = new StringBuilder();
        final StringBuilder nextCursor = new StringBuilder();

        TimedRequest<FindIterable<Document>> timedRequest;

        if (cursor.equals("-1")) {
            TimedRequest.TimedRunnable<FindIterable<Document>> query = new TimedRequest.TimedRunnable<FindIterable<Document>>() {
                @Override
                public FindIterable<Document> run() {
                    return MongoStorage.getDatabase().getCollection("agents").find(filter).sort(new BasicDBObject("_id", sortOrder)).limit(limit);
                }
            };

            final long size = MongoStorage.getDatabase().getCollection("agents").count(filter);
            timedRequest = handleNoCursor(userName, limit, size, query, nextCursor);
        } else if (CursorStore.isValid(userName, cursor)) {
            final CursorId cursorId = new CursorId(cursor);
            timedRequest = handleCursor(cursorId.skip, cursorId.id, userName, nextCursor, prevCursor);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        FindIterable<Document> documents = timedRequest.run();

        return Response.status(Response.Status.OK).entity(MongoResponseBuilder.buildJsonResponse(documents, timedRequest.getElapsed(), prevCursor.toString(), nextCursor.toString())).build();
    }

    @Override
    public Response putAgent(String body,
                             @Context SecurityContext context) {
        if (!MongoStorage.isConnected()) {
            return Response.status(Response.Status.OK).entity("PUT " + context.getUserPrincipal().getName() + "\n\n" + body).build();
        }

        TimedRequest<FindIterable<Document>> timedRequest = new TimedRequest<>();

        /**
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
    public Response getHostCpuInfo(@Context SecurityContext securityContext,
                                 @PathParam("agentId") String agentId,
                                 @QueryParam("size") @DefaultValue("1") String count,
                                 @QueryParam("sort") @DefaultValue("-1") String sort,
                                 @QueryParam("maxTimestamp") String maxTimestamp,
                                 @QueryParam("minTimestamp") String minTimestamp) {
        if (!MongoStorage.isConnected()) {
            return Response.status(Response.Status.OK).entity(agentId + count + sort + maxTimestamp + minTimestamp).build();
        }

        final int size = Integer.valueOf(count);

        final String userName = securityContext.getUserPrincipal().getName();
        final Bson filter = RequestFilters.buildGetFilter(agentId, Arrays.asList(userName), maxTimestamp, minTimestamp);

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

                        Thread.sleep(1000l);
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

    private TimedRequest<FindIterable<Document>> handleNoCursor(final String userName,
                                                                final int limit,
                                                                final long count,
                                                                final TimedRequest.TimedRunnable<FindIterable<Document>> query,
                                                                final StringBuilder nextCursor) {
        return new TimedRequest<>(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                if (count > limit) {
                    MongoCursor cursor = new MongoCursor(count, limit) {
                        @Override
                        public FindIterable<Document> run(int skip) {
                            return  query.run().skip(skip);
                        }
                    };
                    String id = CursorStore.addCursor(userName, cursor);

                    nextCursor.append(limit  + "-" + id);

                }
                return query.run();
            }
        });
    }

    private TimedRequest<FindIterable<Document>> handleCursor(final int skip,
                                                              final String id,
                                                              final String userName,
                                                              final StringBuilder nextCursor,
                                                              final StringBuilder prevCursor) {
        final MongoCursor cursorRequest = CursorStore.getCursor(userName, id);

        int next = skip + cursorRequest.limit;
        if (next < cursorRequest.size) {
            nextCursor.append(next + "-" + id);
        }
        int prev = skip - cursorRequest.limit;
        if (prev > -1) {
            prevCursor.append(prev + "-" + id);
        }

        return new TimedRequest<>(new TimedRequest.TimedRunnable<FindIterable<Document>>() {
            @Override
            public FindIterable<Document> run() {
                return cursorRequest.run(skip);
            }
        });
    }
}
