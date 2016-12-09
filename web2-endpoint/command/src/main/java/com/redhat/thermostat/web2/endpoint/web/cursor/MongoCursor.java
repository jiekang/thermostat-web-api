package com.redhat.thermostat.web2.endpoint.web.cursor;


import org.bson.Document;

import com.mongodb.client.FindIterable;

public abstract class MongoCursor {
    public long size;
    public int limit;

    public MongoCursor(long size, int limit) {
        this.size = size;
        this.limit = limit;
    }

    public abstract FindIterable<Document> run(int skip);
}
