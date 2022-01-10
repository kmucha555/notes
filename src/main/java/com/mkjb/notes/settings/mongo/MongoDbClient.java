package com.mkjb.notes.settings.mongo;


import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class MongoDbClient {

    private static final String NOTES_COLLECTION = "notes";

    private final MongoDatabase db;

    public MongoDbClient(final MongoClient mongoClient,
                         @Value("${mongodb.database}") final String dbName) {
        this.db = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<NoteDocument> collection() {
        return db.getCollection(NOTES_COLLECTION, NoteDocument.class);
    }

}
