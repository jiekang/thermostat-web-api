package com.redhat.thermostat.web2.endpoint.web.cursor;


import org.bson.Document;

import com.mongodb.client.FindIterable;

public abstract class MongoCursor {
    public int limit;

    public MongoCursor(int limit) {
        this.limit = limit;
    }

    public abstract FindIterable<Document> run(int skip);
}
