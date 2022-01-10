package com.mkjb.notes.domain.ports;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteRepository {

    Mono<NoteDocument> find(NoteId noteId);

    Flux<NoteDocument> findAll();

    Mono<String> create(Note note);

    Mono<NoteDocument> update(NoteId noteId, Note note);

    Mono<Void> delete(NoteId noteId);
}
