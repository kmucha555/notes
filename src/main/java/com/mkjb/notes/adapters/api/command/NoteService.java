package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import com.mkjb.notes.domain.ports.NoteFacade;
import com.mkjb.notes.shared.dto.AuthenticatedUser;
import com.mkjb.notes.shared.exception.NoteException;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import static com.mkjb.notes.domain.model.NoteUserRole.OWNER;

@Singleton
class NoteService {

    private final NoteFacade noteFacade;

    NoteService(final NoteFacade noteFacade) {
        this.noteFacade = noteFacade;
    }

    public Mono<NoteIdResponse> createNote(final Authentication authentication, final NoteRequest noteRequest) {
        final var authenticatedUser = AuthenticatedUser.of(authentication);
        final var authUserEmail = NoteUserEmail.of(authenticatedUser.email());
        final var noteTitle = NoteTitle.of(noteRequest.title());
        final var noteContent = NoteContent.of(noteRequest.content());
        final var expireAt = NoteExpireAt.of(noteRequest.expireAt());
        final var noteUser = NoteUser.of(authUserEmail, OWNER);

        return noteFacade.createNote(noteTitle, noteContent, noteUser, expireAt).map(NoteIdResponse::of);
    }

    public Mono<Void> updateNote(final Authentication authentication, final String id, final NoteRequest noteRequest) {
        final var noteId = NoteId.of(id);
        final var authenticatedUser = AuthenticatedUser.of(authentication);
        final var authUserEmail = NoteUserEmail.of(authenticatedUser.email());
        final var note = noteRequest.toDomain();

        return noteFacade
                .findNoteById(noteId)
                .filter(currentNote -> currentNote.isAbleToUpdateNote(authUserEmail))
                .switchIfEmpty(Mono.deferContextual(ctx -> Mono.error(NoteException.forbidden(ctx))))
                .then(Mono.from(noteFacade.updateNote(noteId, note)));
    }

    public Mono<Void> deleteNote(final Authentication authentication, final String id) {
        final var noteId = NoteId.of(id);
        final var authenticatedUser = AuthenticatedUser.of(authentication);
        final var authUserEmail = NoteUserEmail.of(authenticatedUser.email());

        return noteFacade
                .findNoteById(noteId)
                .filter(currentNote -> currentNote.isAbleToDeleteNote(authUserEmail))
                .switchIfEmpty(Mono.deferContextual(ctx -> Mono.error(NoteException.forbidden(ctx))))
                .then(Mono.from(noteFacade.deleteNote(noteId)));
    }

    public Mono<Void> deleteNotes(final Authentication authentication) {
        final var authenticatedUser = AuthenticatedUser.of(authentication);
        final var authUserEmail = NoteUserEmail.of(authenticatedUser.email());

        return noteFacade
                .findNoteByEmail(authUserEmail)
                .filter(currentNote -> currentNote.isAbleToDeleteNote(authUserEmail))
                .switchIfEmpty(Mono.deferContextual(ctx -> Mono.error(NoteException.forbidden(ctx))))
                .map(Note::id)
                .collectList()
                .flatMap(noteIds -> Mono.from(noteFacade.deleteNotes(noteIds)))
                .then();
    }

    public Mono<Void> invite(final Authentication authentication, final String id, final InviteRequest shareRequest) {
        final var noteId = NoteId.of(id);
        final var authenticatedUser = AuthenticatedUser.of(authentication);
        final var authUserEmail = NoteUserEmail.of(authenticatedUser.email());
        final var noteUserToAdd = shareRequest.toDomain();

        return noteFacade
                .findNoteById(noteId)
                .filter(currentNote -> currentNote.isAbleToShareNote(authUserEmail))
                .switchIfEmpty(Mono.deferContextual(ctx -> Mono.error(NoteException.forbidden(ctx))))
                .then(Mono.from(noteFacade.shareNote(noteId, noteUserToAdd)));
    }

}
