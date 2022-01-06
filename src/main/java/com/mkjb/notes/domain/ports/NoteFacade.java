package com.mkjb.notes.domain.ports;

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
        return noteRepository.create(note);
    }
}
