package com.mkjb.notes.domain.ports


import com.mkjb.notes.domain.model.Note
import com.mkjb.notes.domain.model.NoteId
import reactor.core.publisher.Mono

import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteCommandRepository implements NoteCommandRepository {
    private ConcurrentHashMap<NoteId, Note> repository = new ConcurrentHashMap<>()

    @Override
    Mono<NoteId> create(final Note note) {
        def noteId = NoteId.of(UUID.randomUUID().toString())

        def createdNote = Note
                .note()
                .withId(noteId)
                .withTitle(note.title)
                .withContent(note.content)
                .withUsers(note.users)
                .withMetadata(note.metadata)
                .withVersion(note.version)
                .build()

        repository.put(noteId, createdNote)

        return Mono.just(noteId)
    }

    @Override
    Mono<Void> update(final NoteId noteId, final Note note) {
        repository.put(noteId, note)
        return Mono.just(note)
    }

    @Override
    Mono<Void> delete(final NoteId noteId) {
        repository.remove(noteId)
        return Mono.just(noteId).then()
    }
}
