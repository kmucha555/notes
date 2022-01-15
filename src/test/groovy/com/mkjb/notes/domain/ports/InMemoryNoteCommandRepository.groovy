package com.mkjb.notes.domain.ports

import com.mkjb.notes.domain.model.*
import org.bson.types.ObjectId
import reactor.core.publisher.Mono

import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteCommandRepository implements NoteCommandRepository {
    private ConcurrentHashMap<NoteId, Note> repository = new ConcurrentHashMap<>()

    @Override
    Mono<NoteId> create(final NoteTitle noteTitle, final NoteContent noteContent, final List<NoteUser> noteUsers, final ExpireAt expireAt) {
        def noteId = NoteId.of(ObjectId.get().toString())

        def createdNote = Note
                .note()
                .withId(noteId)
                .withTitle(noteTitle)
                .withContent(noteContent)
                .withUsers(noteUsers)
                .withMetadata(NoteMetadata.of(expireAt))
                .withVersion(NoteVersion.INITIAL_VERSION)
                .build()

        repository.put(noteId, createdNote)

        return Mono.just(noteId)
    }

    @Override
    Mono<Void> update(final NoteId noteId, final Note note) {
        repository.put(noteId, note)
        return Mono.empty().then()
    }

    @Override
    Mono<Void> delete(final NoteId noteId) {
        repository.remove(noteId)
        return Mono.empty().then()
    }

    @Override
    Mono<Void> delete() {
        repository.clear()
        return Mono.empty().then()
    }

    def getById(NoteId noteId) {
        return repository.get(noteId)
    }

    def size() {
        return repository.size()
    }

    def clear() {
        repository.clear()
    }
}
