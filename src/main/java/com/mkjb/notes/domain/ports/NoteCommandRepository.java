package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.Note;
import com.mkjb.notes.domain.model.NoteId;
import reactor.core.publisher.Mono;

public interface NoteCommandRepository {

    Mono<NoteId> create(Note note);

    Mono<Note> update(NoteId noteId, Note note);

    Mono<Void> delete(NoteId noteId);
}
