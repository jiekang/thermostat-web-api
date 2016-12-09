package com.redhat.thermostat.web2.endpoint.web.cursor;

public class CursorId {
    public int skip;
    public String id;

    public CursorId(String cursor) {
        this.skip = Integer.valueOf(cursor.substring(0, cursor.indexOf("-")));
        this.id = cursor.substring(cursor.indexOf("-"));
    }
}
