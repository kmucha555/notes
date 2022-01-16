package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.shared.dto.RequestContext;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/notes")
class NoteQueryController {

    private final NoteQueryRepository repository;

    NoteQueryController(final NoteQueryRepository repository) {
        this.repository = repository;
    }

    @Get
    public Flux<NoteDocument> getNotes(Authentication authentication, @RequestBean RequestContext context) {
        return repository
                .findAll(authentication)
                .contextWrite(context.reactorContext());
    }

    @Get("/{noteId}")
    public Mono<NoteDocument> getNoteById(Authentication authentication, @PathVariable String noteId, @RequestBean RequestContext context) {
        return repository
                .findById(authentication, NoteId.of(noteId))
                .contextWrite(context.reactorContext());
    }
}
