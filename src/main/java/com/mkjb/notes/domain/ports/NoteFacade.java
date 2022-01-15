package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.*;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.util.List;

@Singleton
public class NoteFacade {

    private final NoteCommandRepository noteRepository;

    public NoteFacade(final NoteCommandRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Mono<NoteId> createNote(final NoteTitle noteTitle, final NoteContent noteContent, final List<NoteUser> noteUsers, final ExpireAt expireAt) {
        return noteRepository.create(noteTitle, noteContent, noteUsers, expireAt);
    }

    public Mono<Void> updateNote(final NoteId noteId, final Note note) {
        return noteRepository.update(noteId, note);

    }

    public Mono<Void> deleteNote(final NoteId noteId) {
        return noteRepository.delete(noteId);
    }

    public Mono<Void> deleteNotes() {
        return noteRepository.delete();
    }
}
