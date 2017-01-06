package com.redhat.thermostat.web2.endpoint.web.filters;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class RequestFilters {

    public static Bson buildGetFilter(String agentId, List<String> tags) {
        return buildGetFilter(agentId, null, tags, null, null);
    }

    public static Bson buildGetFilter(String agentId, List<String> tags, String maxTimestamp, String minTimestamp) {
        return buildGetFilter(agentId, null, tags, maxTimestamp, minTimestamp);
    }

        /**
         * Builds a filter suitable for get requests
         * @param agentId an agentId to match
         * @param vmId a vmId to match
         * @param maxTimestamp a maximum timestamp
         * @param minTimestamp a minimum timestamp
         * @return the Bson filter
         */
    private static Bson buildGetFilter(String agentId, String vmId, List<String> tags, String maxTimestamp, String minTimestamp) {

        List<Bson> filters = new ArrayList<>();

        if (agentId != null && !agentId.equals("all")) {
            filters.add(eq("agentId", agentId));
        }

        if (vmId != null && !vmId.equals("all")) {
            filters.add(eq("vmId", vmId));
        }

        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                filters.add(or(Filters.exists("tags", false), eq("tags", tag)));
            }
        }

        if (maxTimestamp != null) {
            filters.add(lte("timeStamp", Long.valueOf(maxTimestamp)));
        }
        if (minTimestamp != null) {
            filters.add(gte("timeStamp", Long.valueOf(minTimestamp)));
        }

        return and(filters);
    }
}
