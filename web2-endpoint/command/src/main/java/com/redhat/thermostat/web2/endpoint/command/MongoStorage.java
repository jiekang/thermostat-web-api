package com.redhat.thermostat.web2.endpoint.command;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoStorage {
    private static MongoClient mongoClient;

    private static final String username = "mongodevuser";
    private static final char[] password = "mongodevpassword".toCharArray();
    private static String dbName;

    public static void start(final String dbName, int port) {
        MongoStorage.dbName = dbName;
        MongoCredential credential = MongoCredential.createCredential(username, dbName, password);
        ServerAddress address = new ServerAddress("127.0.0.1", port);
        mongoClient = new MongoClient(address, Arrays.asList(credential), new MongoClientOptions.Builder().serverSelectionTimeout(0).build());
    }

    public static boolean isConnected() {
        try {
            mongoClient.getAddress();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void finish() {
        mongoClient.close();
    }

    public static MongoDatabase getDatabase() {
        return MongoStorage.mongoClient.getDatabase(MongoStorage.dbName);
    }
}