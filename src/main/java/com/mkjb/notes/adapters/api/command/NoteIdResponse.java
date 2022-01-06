package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.NoteId;
import io.micronaut.core.annotation.Introspected;

@Introspected
record NoteIdResponse(String id) {

    static NoteIdResponse of(final NoteId noteId) {
        return new NoteIdResponse(noteId.value());
    }

}
