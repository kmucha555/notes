package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class NoteFacade {

    private final NoteCommandRepository noteRepository;

    public NoteFacade(final NoteCommandRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public Mono<NoteId> createNote(final Note note) {
        return noteRepository.create(note);
    }

    public Mono<Note> updateNote(final NoteId noteId, final Note note) {
        return noteRepository.update(noteId, note);

    }

    public Mono<Void> deleteNote(final NoteId noteId) {
        return noteRepository.delete(noteId);
    }
}
