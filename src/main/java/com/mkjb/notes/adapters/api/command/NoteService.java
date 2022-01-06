package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.ports.NoteFacade;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
class NoteService {

    private final NoteFacade noteFacade;

    NoteService(final NoteFacade noteFacade) {
        this.noteFacade = noteFacade;
    }

    public Mono<NoteId> createNote(final NoteRequest noteRequest) {
        return noteFacade.createNote(noteRequest.toDomain());
    }
}
