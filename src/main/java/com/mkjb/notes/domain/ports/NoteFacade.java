package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.*;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Singleton
public class NoteFacade {

    private final NoteCommandRepository noteRepository;

    public NoteFacade(final NoteCommandRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Mono<NoteId> createNote(final NoteTitle noteTitle, final NoteContent noteContent, final NoteUser noteUser, final NoteExpireAt expireAt) {
        return noteRepository.create(noteTitle, noteContent, noteUser, expireAt);
    }

    public Mono<Note> findNoteById(final NoteId noteId) {
        return noteRepository.findByNoteId(noteId);
    }

    public Flux<Note> findNoteByEmail(final NoteUserEmail noteUserEmail) {
        return noteRepository.findByUserEmail(noteUserEmail);
    }

    public Mono<Void> updateNote(final NoteId noteId, final Note note) {
        return noteRepository.update(noteId, note);
    }

    public Mono<Void> deleteNote(final NoteId noteId) {
        return noteRepository.delete(noteId);
    }

    public Mono<Void> deleteNotes(final List<NoteId> noteIds) {
        return noteRepository.deleteAll(noteIds);
    }

    public Mono<Void> shareNote(final NoteId noteId, final NoteUser noteUserToAdd) {
        return noteRepository.share(noteId, noteUserToAdd);
    }
}
