package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.ExpireAt;
import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.ports.NoteRepository;
import com.mkjb.notes.settings.mongo.MongoDbClient;
import com.mongodb.client.result.InsertOneResult;
import jakarta.inject.Singleton;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ClockProvider;
import java.time.Instant;
import java.util.UUID;

import static com.mkjb.notes.settings.reactor.ContextLogger.logWithCtx;

@Singleton
class MongoNoteRepository implements NoteRepository {

    private static final Logger log = LoggerFactory.getLogger(MongoNoteRepository.class);
    private static final String NOTES_COLLECTION = "notes";

    private final MongoDbClient mongoClient;
    private final ClockProvider clockProvider;

    MongoNoteRepository(final MongoDbClient mongoClient, final ClockProvider clockProvider) {
        this.mongoClient = mongoClient;
        this.clockProvider = clockProvider;
    }

    @Override
    public Mono<NoteDocument> find(final NoteId noteId) {
        return null;
    }

    @Override
    public Flux<Note> findAll() {
        return Flux.deferContextual(ctx ->
                        Flux
                                .from(mongoClient.collection(NOTES_COLLECTION, NoteDocument.class).find())
//                            .onErrorMap(t -> NoteException.internal(context, t.getMessage()))
                                .doOnEach(logWithCtx(noteDocument -> log.debug("Note with id '{}' fetched", noteDocument.getId())))
                                .map(NoteDocument::toDomain)
        );

    }

    @Override
    public Mono<NoteId> create(final Note note) {
        return Mono.deferContextual(ctx -> {
                    final var expireAt = note.getMetadata().expireAt().map(ExpireAt::value).orElse(null);

                    final var metadataDocument =
                            MetadataDocument
                                    .metadataDocument()
                                    .withCreatedAt(Instant.now())
                                    .withExpiredAt(expireAt)
                                    .build();

                    final var userDocuments =
                            note
                                    .getUsers()
                                    .stream()
                                    .map(u -> new UserDocument(u.email(), u.role().name()))
                                    .toList();

                    final var noteDocument =
                            NoteDocument
                                    .noteDocument()
                                    .withId(UUID.randomUUID().toString())
                                    .withTitle(note.getTitle().value())
                                    .withContent(note.getContent().value())
                                    .withUsers(userDocuments)
                                    .withMetadata(metadataDocument)
                                    .withVersion(note.getVersion().value())
                                    .build();


                    return Mono
                            .from(mongoClient.collection(NOTES_COLLECTION, NoteDocument.class).insertOne(noteDocument))
//                            .onErrorMap(t -> NoteException.internal(context, t.getMessage()))
                            .map(InsertOneResult::getInsertedId)
                            .map(BsonValue::asString)
                            .map(BsonString::getValue)
                            .map(NoteId::of)
                            .doOnEach(logWithCtx(noteId -> log.info("New note with id '{}' created", noteId)));
                }
        );
    }

    @Override
    public Mono<NoteDocument> update(final NoteId noteId, final Note note) {
        return null;
    }

    @Override
    public Mono<Void> delete(final NoteId noteId) {
        return null;
    }
}
