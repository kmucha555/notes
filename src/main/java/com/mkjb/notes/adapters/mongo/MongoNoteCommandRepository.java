package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.*;
import com.mkjb.notes.domain.ports.NoteCommandRepository;
import com.mkjb.notes.settings.mongo.MongoDbClient;
import com.mkjb.notes.shared.exception.NoteException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.inject.Singleton;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ClockProvider;
import java.time.Instant;
import java.util.List;

import static com.mkjb.notes.settings.reactor.ContextLogger.logWithCtx;

@Singleton
class MongoNoteCommandRepository implements NoteCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(MongoNoteCommandRepository.class);
    public static final int MONGO_DUPLICATE_KEY_ERROR_CODE = 11000;

    private final MongoDbClient mongoClient;
    private final ClockProvider clockProvider;

    MongoNoteCommandRepository(final MongoDbClient mongoClient, final ClockProvider clockProvider) {
        this.mongoClient = mongoClient;
        this.clockProvider = clockProvider;
    }

    @Override
    public Mono<NoteId> create(final NoteTitle noteTitle, final NoteContent noteContent, final NoteUser noteUser, final NoteExpireAt expireAt) {
        return Mono.deferContextual(ctx -> {
                    final var userDocument = UserDocument.of(noteUser);
                    final var createdAt = Instant.now(clockProvider.getClock());

                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withCreatedAt(createdAt)
                                    .withModifiedAt(createdAt)
                                    .withExpiredAt(expireAt.getOrNull())
                                    .build();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withTitle(noteTitle.value())
                                    .withContent(noteContent.value())
                                    .withUsers(List.of(userDocument))
                                    .withMetadata(metadataDocument)
                                    .withVersion(NoteVersion.INITIAL_VERSION.value())
                                    .build();

                    return Mono
                            .from(mongoClient.collection().insertOne(noteDocument))
                            .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                            .map(InsertOneResult::getInsertedId)
                            .map(BsonValue::asObjectId)
                            .map(BsonObjectId::getValue)
                            .map(ObjectId::toString)
                            .doOnEach(logWithCtx(noteId -> log.info("New note with id '{}' created", noteId)))
                            .map(NoteId::of);
                }
        );
    }

    @Override
    public Mono<Note> findByNoteId(final NoteId noteId) {
        return Mono.deferContextual(ctx ->
                Mono
                        .from(mongoClient.collection().find(filterByNoteId(noteId)))
                        .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                        .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                        .map(NoteDocument::toDomain)
                        .doOnEach(logWithCtx(note -> log.info("The note with id '{}' fetched", note.id())))
        );
    }

    @Override
    public Flux<Note> findByUserEmail(final NoteUserEmail noteUserEmail) {
        return Flux.deferContextual(ctx ->
                Flux
                        .from(mongoClient.collection().find(filterByUserEmail(noteUserEmail.value())))
                        .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                        .map(NoteDocument::toDomain)
                        .doOnEach(logWithCtx(note -> log.info("The note with id '{}' fetched", note.id())))
        );
    }

    @Override
    public Mono<Void> update(final NoteId noteId, final Note note) {
        return Mono.deferContextual(ctx -> {
                    final var modifiedAt = Instant.now(clockProvider.getClock());

                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withModifiedAt(modifiedAt)
                                    .withExpiredAt(note.metadata().expireAtValue().getOrNull())
                                    .build();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withId(noteId.toObjectId())
                                    .withTitle(note.title().value())
                                    .withContent(note.content().value())
                                    .withVersion(note.version().increment())
                                    .build();

                    final var filter = Filters.and(filterByNoteId(noteId), filterByVersion(note.version()));
                    final var noteUpdate = new Document(noteDocument.toNoteUpdate());
                    final var metadataUpdate = new Document(metadataDocument.toMetadataUpdate());
                    final var update = new Document("$set", Updates.combine(noteUpdate, metadataUpdate));

                    return Mono
                            .from(mongoClient.collection().updateOne(filter, update, new UpdateOptions().upsert(true)))
                            .onErrorMap(t -> {
                                if (t instanceof MongoWriteException writeException && writeException.getError().getCode() == MONGO_DUPLICATE_KEY_ERROR_CODE) {
                                    return NoteException.conflict(ctx, noteId);
                                }
                                return NoteException.internal(ctx, noteId, t.getMessage());
                            })
                            .filter(this::hasSuccessfullyUpdatedDocument)
                            .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                            .doOnEach(logWithCtx(a -> log.info("Note with id '{}' successfully updated", noteId)))
                            .then();
                }
        );
    }

    @Override
    public Mono<Void> delete(final NoteId noteId) {
        return Mono.deferContextual(ctx -> {
            final var filter = Filters.and(filterByNoteId(noteId));

            return Mono
                    .from(mongoClient.collection().deleteOne(filter))
                    .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                    .filter(this::hasSuccessfullyDeletedDocument)
                    .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                    .doOnEach(logWithCtx(result -> log.info("Note with id '{}' successfully deleted", noteId)))
                    .then();
        });
    }

    @Override
    public Mono<Void> deleteAll(final List<NoteId> noteIds) {
        return Mono.deferContextual(ctx -> {
            final var filter = Filters.and(filterByNoteIds(noteIds));

            return Mono
                    .from(mongoClient.collection().deleteMany(filter))
                    .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                    .filter(this::hasSuccessfullyDeletedDocument)
                    .doOnEach(logWithCtx(result -> log.info("Note with ids '{}' successfully deleted", noteIds)))
                    .then();
        });
    }

    @Override
    public Mono<Void> share(final NoteId noteId, final NoteUser noteUser) {
        return Mono.deferContextual(ctx -> {
            final var modifiedAt = Instant.now(clockProvider.getClock());

            final var userDocument = UserDocument.of(noteUser);

            final var metadataDocument =
                    MetadataDocument
                            .metadataDocument()
                            .withModifiedAt(modifiedAt)
                            .build();

            final var filter = Filters.and(filterByNoteId(noteId));

            final var pushNewUser = Updates.addToSet("users", userDocument);
            final var updateModifiedAt = new Document("$set", new Document(metadataDocument.toMetadataUpdate()));
            final var update = Updates.combine(updateModifiedAt, pushNewUser);

            return Mono
                    .from(mongoClient.collection().updateOne(filter, update))
                    .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                    .filter(this::hasSuccessfullyUpdatedDocument)
                    .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                    .doOnEach(logWithCtx(a -> log.info("Note with id '{}' successfully shared", noteId)))
                    .then();
        });
    }

    private boolean hasSuccessfullyUpdatedDocument(final UpdateResult updateResult) {
        return updateResult.getModifiedCount() > 0;
    }

    private boolean hasSuccessfullyDeletedDocument(final DeleteResult deleteResult) {
        return deleteResult.getDeletedCount() > 0;
    }

}
