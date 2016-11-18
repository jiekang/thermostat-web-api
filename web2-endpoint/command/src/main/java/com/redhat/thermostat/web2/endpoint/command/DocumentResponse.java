package com.redhat.thermostat.web2.endpoint.command;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class DocumentResponse {
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

    public static String build(FindIterable<Document> items, long elapsed) {
        StringBuilder s = new StringBuilder();
        int i = 0;

        s.append("{\"response\" : {");
        for (Document item : items) {
            s.append("\"" + i + "\" : " + item.toJson() + ",");
            i++;
        }
        if (i != 0) {
            s.deleteCharAt(s.length() - 1);
        }


        s.append("},\"time\" : " + elapsed + "}");

        return s.toString();
    }
}