package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.*;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NoteCommandRepository {

    Mono<NoteId> create(NoteTitle noteTitle, NoteContent noteContent, NoteUser noteUser, NoteExpireAt expireAt);

    Mono<Note> findByNoteId(NoteId noteId);

    Flux<Note> findByUserEmail(NoteUserEmail noteUserEmail);

    Mono<Void> update(NoteId noteId, Note note);

    Mono<Void> delete(NoteId noteId);

    Mono<Void> deleteAll(List<NoteId> noteIds);

    Mono<Void> share(NoteId noteId, NoteUser noteUser);

    default Bson filterByNoteId(final NoteId noteId) {
        return Filters.eq(noteId.toObjectId());
    }

    default Bson filterByNoteIds(final List<NoteId> noteIds) {
        return Filters.in("_id", noteIds);
    }

    default Bson filterByVersion(final NoteVersion version) {
        return Filters.eq("version", version.value());
    }

    default Bson filterByUserEmail(final String email) {
        return Filters.eq("users.email", email);
    }
}
