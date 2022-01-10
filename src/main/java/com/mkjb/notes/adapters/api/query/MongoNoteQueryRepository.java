package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.settings.mongo.MongoDbClient;
import com.mkjb.notes.shared.exception.NoteException;
import com.mongodb.client.model.Filters;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.mkjb.notes.settings.reactor.ContextLogger.logWithCtx;

@Singleton
class MongoNoteQueryRepository implements NoteQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(MongoNoteQueryRepository.class);

    private final MongoDbClient mongoClient;

    MongoNoteQueryRepository(final MongoDbClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Mono<NoteDocument> find(final NoteId noteId) {
        return Mono.deferContextual(ctx -> {
                    final var filterByNoteId = Filters.eq(noteId.value());

                    return Mono
                            .from(mongoClient.collection().find(filterByNoteId))
                            .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                            .doOnEach(logWithCtx(noteDocument -> log.debug("Note with id '{}' fetched", noteDocument.getId())));
                }
        );
    }

    @Override
    public Flux<NoteDocument> findAll() {
        return Flux.deferContextual(ctx ->
                Flux
                        .from(mongoClient.collection().find())
                        .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                        .doOnEach(logWithCtx(noteDocument -> log.debug("Note with id '{}' fetched", noteDocument.getId())))
        );
    }

}
