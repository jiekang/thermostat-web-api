package com.redhat.thermostat.web2.endpoint.web.response;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class ResponseBuilder {
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

    public static String buildJsonFromDocuments(FindIterable<Document> documents, long elapsed) {
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


        s.append("},\"time\" : " + elapsed + "}");

        return s.toString();
    }
}
