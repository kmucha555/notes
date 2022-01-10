package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.*;
import com.mkjb.notes.domain.ports.NoteCommandRepository;
import com.mkjb.notes.settings.mongo.MongoDbClient;
import com.mkjb.notes.shared.exception.NoteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import jakarta.inject.Singleton;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.mkjb.notes.settings.reactor.ContextLogger.logWithCtx;

@Singleton
class MongoNoteCommandRepository implements NoteCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(MongoNoteCommandRepository.class);

    private final MongoDbClient mongoClient;

    MongoNoteCommandRepository(final MongoDbClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Mono<NoteId> create(final Note note) {
        return Mono.deferContextual(ctx -> {
                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withCreatedAt(Instant.now())
                                    .withExpiredAt(getExpireAt(note.getMetadata()))
                                    .build();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withId(UUID.randomUUID().toString())
                                    .withTitle(note.getTitle().value())
                                    .withContent(note.getContent().value())
                                    .withUsers(getUserDocuments(note.getUsers()))
                                    .withMetadata(metadataDocument)
                                    .withVersion(NoteVersion.INITIAL_VERSION.value())
                                    .build();

                    return Mono
                            .from(mongoClient.collection().insertOne(noteDocument))
                            .onErrorMap(t -> NoteException.internal(ctx, note.getId(), t.getMessage()))
                            .map(InsertOneResult::getInsertedId)
                            .map(BsonValue::asString)
                            .map(BsonString::getValue)
                            .doOnEach(logWithCtx(noteId -> log.info("New note with id '{}' created", noteId)))
                            .map(NoteId::of);
                }
        );
    }

    @Override
    public Mono<Note> update(final NoteId noteId, final Note note) {
        return Mono.deferContextual(ctx -> {
                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withModifiedAt(Instant.now())
                                    .withExpiredAt(getExpireAt(note.getMetadata()))
                                    .build();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withId(noteId.value())
                                    .withTitle(note.getTitle().value())
                                    .withContent(note.getContent().value())
                                    .withUsers(getUserDocuments(note.getUsers()))
                                    .withMetadata(metadataDocument)
                                    .withVersion(note.getVersion().increment())
                                    .build();

                    final var filterBy = Filters.and(Filters.eq(noteId.value()), Filters.eq("version", note.getVersion().value()));
                    final var updateDocument = new Document("$set", noteDocument);

                    return Mono
                            .from(mongoClient.collection().findOneAndUpdate(filterBy, updateDocument))
                            .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                            .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                            .doOnEach(logWithCtx(a -> log.info("Note with id '{}' successfully updated", noteId)))
                            .map(NoteDocument::toDomain);
                }
        );

    }

    @Override
    public Mono<Void> delete(final NoteId noteId) {
        return Mono.deferContextual(ctx ->
                        Mono
                                .from(mongoClient.collection().deleteOne(Filters.eq(noteId.value())))
                                .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                                .filter(this::hasSuccessfullyDeletedDocument)
                                .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                                .doOnEach(logWithCtx(result -> log.info("Note with id '{}' successfully deleted", noteId)))
                                .then()
        );
    }

    private boolean hasSuccessfullyDeletedDocument(final DeleteResult deleteResult) {
        return deleteResult.getDeletedCount() > 0;
    }

    private List<UserDocument> getUserDocuments(final Set<NoteUser> users) {
        return users
                .stream()
                .map(UserDocument::of)
                .toList();
    }

    private Instant getExpireAt(final NoteMetadata metadata) {
        return metadata
                .expireAt()
                .map(ExpireAt::value)
                .orElse(null);
    }

}
