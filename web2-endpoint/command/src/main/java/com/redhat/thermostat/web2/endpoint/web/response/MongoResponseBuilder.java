package com.redhat.thermostat.web2.endpoint.web.response;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class MongoResponseBuilder {

    /**
     * Responses in JSON:
     * {
     * "response" : {
     * "0" : {
     * ...
     * },
     * "1" : {
     * ...
     * },
     * ...
     * },
     * <p>
     * "time" : elapsed
     * }
     */
    public static String buildJsonResponse(FindIterable<Document> documents, long elapsed) {
        StringBuilder s = new StringBuilder();

        s.append(buildJsonDocuments(documents));
        s.append("},\"time\" : " + elapsed + "}");

        return s.toString();
    }

    public static String buildJsonResponse(FindIterable<Document> documents, long elapsed, String prevCursor, String nextCursor) {
        StringBuilder s = new StringBuilder();

        s.append(buildJsonDocuments(documents));
        s.append(buildKeyAddition("time", "" + elapsed));

        if (prevCursor.length() > 0) {
            s.append(buildKeyAddition("prevCursor", prevCursor));
        }
        if (nextCursor.length() > 0) {
            s.append(buildKeyAddition("nextCursor", nextCursor));
        }

        s.append("}");

        return s.toString();
    }

    private static String buildJsonDocuments(FindIterable<Document> documents) {
        StringBuilder s = new StringBuilder();
        int i = 0;

        s.append("{\"response\" : {");
        for (Document document : documents) {
            String response;
            if (document.containsKey("item")) {
                response = ((Document)document.get("item")).toJson();
            } else {
                response = document.toJson();
            }
            s.append("\"" + i + "\" : " + response + ",");
            i++;
        }

        if (i != 0) {
            s.deleteCharAt(s.length() - 1);
        }
        return s.toString();
    }

    private static String buildKeyAddition(String key, String value) {
        return new String(".\"" + key + "\" : " + value);
    }
}
