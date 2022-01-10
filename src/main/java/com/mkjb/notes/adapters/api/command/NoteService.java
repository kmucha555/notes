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

    public Mono<NoteIdResponse> createNote(final NoteRequest noteRequest) {
        return noteFacade
                .createNote(noteRequest.toDomain())
                .map(NoteIdResponse::of);
    }

    public Mono<NoteResponse> updateNote(final String noteId, final NoteRequest noteRequest) {
        return noteFacade
                .updateNote(NoteId.of(noteId), noteRequest.toDomain())
                .map(NoteResponse::of);
    }

    public Mono<Void> deleteNote(final String noteId) {
        return noteFacade.deleteNote(NoteId.of(noteId));
    }
}
