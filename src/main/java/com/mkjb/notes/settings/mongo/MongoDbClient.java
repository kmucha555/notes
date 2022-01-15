package com.mkjb.notes.settings.mongo;


import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Singleton
public class MongoDbClient {

    private static final String NOTES_COLLECTION = "notes";

    private final MongoDatabase db;

    public MongoDbClient(final MongoClient mongoClient,
                         @Value("${mongodb.database}") final String dbName) {
        this.db = mongoClient.getDatabase(dbName);
        createExpireAtIndex();
    }

    public MongoCollection<NoteDocument> collection() {
        return db.getCollection(NOTES_COLLECTION, NoteDocument.class);
    }

    private void createExpireAtIndex() {
        final var expireAtField = Indexes.ascending("metadata.expireAt");
        final var expireAtIndexOptions = new IndexOptions();
        expireAtIndexOptions.expireAfter(0L, TimeUnit.SECONDS);
        Mono.from(db.getCollection(NOTES_COLLECTION).createIndex(expireAtField, expireAtIndexOptions)).block();
    }

}
