package com.mkjb.notes.settings.mongo;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.bson.Document;

@Singleton
public class MongoDbClient {
    private final MongoDatabase db;

    public MongoDbClient(final MongoClient mongoClient,
                         @Value("${mongodb.database}") final String dbName) {
        this.db = mongoClient.getDatabase(dbName);
    }

    public <T> MongoCollection<T> collection(final String collectionName, Class<T> documentClass) {
        return db.getCollection(collectionName, documentClass);
    }

    public <T> MongoCollection<Document> collection(final String collectionName) {
        return db.getCollection(collectionName);
    }
}
