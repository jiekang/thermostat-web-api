package com.redhat.thermostat.web2.endpoint.web.json;

public class DocumentBuilder {
    public static String addTags(String content, String... tags) {
        StringBuilder tagBuilder = new StringBuilder();
        tagBuilder.append(",\"tags\":[\"agent\"");
        for (String tag : tags) {
            tagBuilder.append(", \"").append(tag).append("\"");
        }
        tagBuilder.append("]}");

        StringBuilder contentBuilder = new StringBuilder(content);
        contentBuilder.insert(contentBuilder.length() - 1, tagBuilder.toString());
        return contentBuilder.toString();
    }
}
