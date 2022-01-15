package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.*;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NoteCommandRepository {

    Mono<NoteId> create(NoteTitle noteTitle, NoteContent noteContent, List<NoteUser> noteUsers, ExpireAt expireAt);

    Mono<Void> update(NoteId noteId, Note note);

    Mono<Void> delete(NoteId noteId);

    default Bson filterByNoteId(NoteId noteId) {
        return Filters.eq(noteId.toObjectId());
    }

    default Bson filterByVersion(NoteVersion version) {
        return Filters.eq("version", version.value());
    }
}
