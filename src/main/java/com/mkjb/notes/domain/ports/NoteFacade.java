package com.mkjb.notes.domain.ports;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class NoteFacade {

    private final NoteRepository noteRepository;

    public NoteFacade(final NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public Mono<NoteId> createNote(final Note note) {
        return noteRepository
                .create(note)
                .map(NoteId::of);
    }

    public Mono<Note> updateNote(final NoteId noteId, final Note note) {
        return noteRepository
                .update(noteId, note)
                .map(NoteDocument::toDomain);
    }

    public Mono<Void> deleteNote(final NoteId noteId) {
        return noteRepository.delete(noteId);
    }
}
