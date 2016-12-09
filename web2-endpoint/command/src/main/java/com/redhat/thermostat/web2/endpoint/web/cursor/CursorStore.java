package com.redhat.thermostat.web2.endpoint.web.cursor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CursorStore {
    private static final Map<String, Map<String, MongoCursor>> userMap = new HashMap<>();

    public static String addCursor(String user, MongoCursor runnable) {
        Map<String, MongoCursor> cursorMap = userMap.get(user);
        if (null == cursorMap) {
            cursorMap = new HashMap<>();
            userMap.put(user, cursorMap);
        }
        String id = UUID.randomUUID().toString();
        while (cursorMap.containsKey(id)) {
            id = UUID.randomUUID().toString();
        }

        cursorMap.put(id, runnable);

        return id;
    }

    public static MongoCursor getCursor(String user, String id) {
        Map<String, MongoCursor> cursorMap = userMap.get(user);
        if (null == cursorMap) {
            return null;
        }

        return cursorMap.get(id);
    }

    public static boolean isValid(String user, String cursor) {
        try {
            int front = Integer.valueOf(cursor.substring(0, cursor.indexOf("-")));
        } catch (NumberFormatException e) {
            return false;
        }

        return userMap.containsKey(user) && userMap.get(user).containsKey(cursor.substring(cursor.indexOf("-") + 1));
    }
}
