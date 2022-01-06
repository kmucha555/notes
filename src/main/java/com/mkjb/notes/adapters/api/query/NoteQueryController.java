package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.ports.NoteRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/notes")
class NoteQueryController {

    private final NoteRepository repository;

    NoteQueryController(final NoteRepository repository) {
        this.repository = repository;
    }

    @Get
    public Flux<NoteDocument> getNotes() {
        return repository.findAll();
    }

    @Get("/{noteId}")
    public Flux<NoteDocument> getNotesById(@PathVariable NoteId noteId) {
        return repository.findAll();
    }
}
