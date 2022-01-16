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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            tags = "Notes",
            summary = "Retrieving all user notes",
            description = "Retrieves a list user's notes",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Flux<NoteDocument> getNotes(Authentication authentication, @RequestBean RequestContext context) {
        return repository
                .findAll(authentication)
                .contextWrite(context.reactorContext());
    }

    @Get("/{noteId}")
    @Operation(
            tags = "Notes",
            summary = "Retrieving user note details",
            description = "Retrieves a specified user's note",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<NoteDocument> getNoteById(Authentication authentication, @PathVariable String noteId, @RequestBean RequestContext context) {
        return repository
                .findById(authentication, NoteId.of(noteId))
                .contextWrite(context.reactorContext());
    }
}
