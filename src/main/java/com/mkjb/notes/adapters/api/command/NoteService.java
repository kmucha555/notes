package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.ExpireAt;
import com.mkjb.notes.domain.model.NoteContent;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.model.NoteTitle;
import com.mkjb.notes.domain.ports.NoteFacade;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import static com.mkjb.notes.domain.model.NoteUser.validateExactlyOneUserInOwnerRole;

@Singleton
class NoteService {

    private final NoteFacade noteFacade;

    NoteService(final NoteFacade noteFacade) {
        this.noteFacade = noteFacade;
    }

    public Mono<NoteIdResponse> createNote(final Authentication authentication, final NoteRequest noteRequest) {
        final var noteTitle = NoteTitle.of(noteRequest.title());
        final var noteContent = NoteContent.of(noteRequest.content());
        final var noteUsers = noteRequest.users().stream().map(NoteRequest.User::toDomain).toList();
        final var expireAt = ExpireAt.of(noteRequest.expireAt());

        validateExactlyOneUserInOwnerRole(noteUsers);

        return noteFacade
                .createNote(noteTitle, noteContent, noteUsers, expireAt)
                .map(NoteIdResponse::of);
    }

    public Mono<Void> updateNote(final String noteId, final NoteRequest noteRequest) {
        return noteFacade.updateNote(NoteId.of(noteId), noteRequest.toDomain());
    }

    public Mono<Void> deleteNote(final String noteId) {
        return noteFacade.deleteNote(NoteId.of(noteId));
    }

    public Mono<Void> deleteNotes() {
        return noteFacade.deleteNotes();
    }
}
