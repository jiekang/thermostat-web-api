package com.redhat.thermostat.web2.endpoint.command;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoStorage {
    private MongoClient mongoClient;
    private final String dbName;
    private final int port;

    private String username = "mongodevuser";
    private char[] password = "mongodevpassword".toCharArray();

    private static MongoDatabase database;

    public MongoStorage(String dbName, int port) {
        this.dbName = dbName;
        this.port = port;
    }

    public void start() {
        MongoCredential credential = MongoCredential.createCredential(username, dbName, password);
        ServerAddress address = new ServerAddress("127.0.0.1", port);
        mongoClient = new MongoClient(address, Arrays.asList(credential));
        database = mongoClient.getDatabase(dbName);
    }

    public void finish() {
        mongoClient.close();
    }

    public static MongoDatabase getDatabase() {
        return MongoStorage.database;
    }
}