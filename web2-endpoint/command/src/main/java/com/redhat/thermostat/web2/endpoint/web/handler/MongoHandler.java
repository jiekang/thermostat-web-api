package com.redhat.thermostat.web2.endpoint.web.handler;

import java.util.Arrays;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.redhat.thermostat.web2.endpoint.command.MongoStorage;
import com.redhat.thermostat.web2.endpoint.web.cursor.CursorId;
import com.redhat.thermostat.web2.endpoint.web.cursor.MongoCursor;
import com.redhat.thermostat.web2.endpoint.web.cursor.CursorStore;
import com.redhat.thermostat.web2.endpoint.web.filters.RequestFilters;
import com.redhat.thermostat.web2.endpoint.web.request.TimedRequest;
import com.redhat.thermostat.web2.endpoint.web.response.ResponseBuilder;

public class MongoHandler implements StorageHandler {

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

        return Response.status(Response.Status.OK).entity(ResponseBuilder.buildJsonResponse(documents, timedRequest.getElapsed(), prevCursor.toString(), nextCursor.toString())).build();
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
