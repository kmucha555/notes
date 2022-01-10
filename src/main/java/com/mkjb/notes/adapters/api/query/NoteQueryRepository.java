package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

interface NoteQueryRepository {

    Mono<NoteDocument> find(NoteId noteId);

    Flux<NoteDocument> findAll();
}
