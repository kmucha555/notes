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
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.validation.ClockProvider;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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
    public Mono<NoteId> create(final NoteTitle noteTitle, final NoteContent noteContent, final List<NoteUser> noteUsers, final ExpireAt expireAt) {
        return Mono.deferContextual(ctx -> {
                    final var userDocuments = toUserDocuments(noteUsers);

                    final var createdAt = Instant.now(clockProvider.getClock());

                    final var metadataDocument = MetadataDocument
                            .metadataDocument()
                            .withCreatedAt(createdAt)
                            .withModifiedAt(createdAt)
                            .withExpiredAt(expireAt.getOrNull())
                            .build();

                    final var noteDocument = NoteDocument
                            .noteDocument()
                            .withTitle(noteTitle.value())
                            .withContent(noteContent.value())
                            .withUsers(userDocuments)
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
    public Mono<Void> update(final NoteId noteId, final Note note) {
        return Mono.deferContextual(ctx -> {
                    final var userDocuments = toUserDocuments(note.getUsers());

                    final var modifiedAt = Instant.now(clockProvider.getClock());

                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withModifiedAt(modifiedAt)
                                    .withExpiredAt(note.getMetadata().expireAtValue().getOrNull())
                                    .build();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withId(noteId.toObjectId())
                                    .withTitle(note.getTitle().value())
                                    .withContent(note.getContent().value())
                                    .withUsers(userDocuments)
                                    .withMetadata(metadataDocument)
                                    .withVersion(note.getVersion().increment())
                                    .build();

                    final Map<String, Object> stringInstantMap = Map.of(
                            "metadata.modifiedAt", Instant.now(clockProvider.getClock()),
                            "metadata.expireAt", note.getMetadata().expireAtValue().getOrNull()
                    );
                    final var filter = Filters.and(filterByNoteId(noteId), filterByVersion(note.getVersion()));

                    final Bson combine = Updates.combine(new Document(noteDocument.toMap()), new Document(stringInstantMap));
                    final var updateDocument = new Document("$set", combine);

                    return Mono
                            .from(mongoClient.collection().updateOne(filter, updateDocument, new UpdateOptions().upsert(true)))
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
        return Mono.deferContextual(ctx ->
                Mono
                        .from(mongoClient.collection().deleteOne(filterByNoteId(noteId)))
                        .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                        .filter(this::hasSuccessfullyDeletedDocument)
                        .switchIfEmpty(Mono.error(() -> NoteException.notFound(ctx, noteId)))
                        .doOnEach(logWithCtx(result -> log.info("Note with id '{}' successfully deleted", noteId)))
                        .then()
        );
    }

    private boolean hasSuccessfullyUpdatedDocument(final UpdateResult updateResult) {
        return updateResult.getModifiedCount() > 0;
    }

    private boolean hasSuccessfullyDeletedDocument(final DeleteResult deleteResult) {
        return deleteResult.getDeletedCount() > 0;
    }

    private List<UserDocument> toUserDocuments(final List<NoteUser> users) {
        return users.stream().map(UserDocument::of).toList();
    }

}
