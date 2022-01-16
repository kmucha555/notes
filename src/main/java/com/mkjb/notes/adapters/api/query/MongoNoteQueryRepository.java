package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.settings.mongo.MongoDbClient;
import com.mkjb.notes.shared.dto.AuthenticatedUser;
import com.mkjb.notes.shared.dto.RequestContext;
import com.mkjb.notes.shared.exception.NoteException;
import com.mongodb.client.model.Filters;
import io.micronaut.security.authentication.Authentication;
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
    public Mono<NoteDocument> findById(final Authentication authentication, final NoteId noteId) {
        return Mono.deferContextual(ctx -> {
            final var authUser = AuthenticatedUser.of(authentication);

            return Mono
                    .from(mongoClient.collection().find(Filters.and(findByNoteId(noteId), findByUserEmailId(authUser))))
                    .onErrorMap(t -> NoteException.internal(ctx, noteId, t.getMessage()))
                    .switchIfEmpty(Mono.error(NoteException.notFound(ctx, noteId)))
                    .doOnEach(logWithCtx(noteDocument -> log.debug("Note with id '{}' fetched", noteDocument.getId())));
        });
    }

    @Override
    public Flux<NoteDocument> findAll(final Authentication authentication) {
        return Flux.deferContextual(ctx -> {
            final var context = ctx.get(RequestContext.class);
            final var authUser = AuthenticatedUser.of(authentication);

            return Flux
                    .from(
                            mongoClient
                                    .collection()
                                    .find(findByUserEmailId(authUser))
                                    .sort(sort(context))
                                    .skip(pageNumber(context))
                                    .limit(pageSize(context))
                    )
                    .onErrorMap(t -> NoteException.internal(ctx, t.getMessage()))
                    .doOnEach(logWithCtx(noteDocument -> log.debug("Note with id '{}' fetched", noteDocument.getId())));
        });
    }

}
