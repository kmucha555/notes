package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.ports.NoteRepository;
import com.mkjb.notes.shared.dto.RequestContext;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.RequestBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/notes")
class NoteQueryController {

    private final NoteRepository repository;

    NoteQueryController(final NoteRepository repository) {
        this.repository = repository;
    }

    @Get
    public Flux<NoteDocument> getNotes(@RequestBean RequestContext context) {
        return repository
                .findAll()
                .contextWrite(context.reactorContext());
    }

    @Get("/{noteId}")
    public Mono<NoteDocument> getNoteById(@PathVariable String noteId, @RequestBean RequestContext context) {
        return repository
                .find(NoteId.of(noteId))
                .contextWrite(context.reactorContext());
    }
}
