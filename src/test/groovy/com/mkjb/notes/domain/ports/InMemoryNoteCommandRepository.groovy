package com.mkjb.notes.domain.ports

import com.mkjb.notes.domain.model.*
import org.bson.types.ObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteCommandRepository implements NoteCommandRepository {
    private ConcurrentHashMap<NoteId, Note> repository = new ConcurrentHashMap<>()

    @Override
    Mono<NoteId> create(final NoteTitle noteTitle, final NoteContent noteContent, final NoteUser noteUser, final NoteExpireAt expireAt) {
        def noteId = NoteId.of(ObjectId.get().toString())

        def createdNote = Note
                .note()
                .withId(noteId)
                .withTitle(noteTitle)
                .withContent(noteContent)
                .withUsers([noteUser])
                .withMetadata(NoteMetadata.of(expireAt))
                .withVersion(NoteVersion.INITIAL_VERSION)
                .build()

        repository.put(noteId, createdNote)

        return Mono.just(noteId)
    }

    @Override
    Mono<Note> findByNoteId(final NoteId noteId) {
        return Mono.just(repository.get(noteId))
    }

    @Override
    Flux<Note> findByUserEmail(final NoteUserEmail noteUserEmail) {
        return Flux.fromIterable(
                repository
                        .find { entry ->
                            entry.value.users()
                                    .find { noteUser -> noteUser.email() == noteUserEmail }
                        }
                        .collect { it -> it.value }
        )
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
    Mono<Void> deleteAll(final List<NoteId> noteIds) {
        repository.clear()
        return Mono.empty().then()
    }

    @Override
    Mono<Void> share(final NoteId noteId, final NoteUser noteUser) {
        repository.get(noteId).users().add(noteUser)

        return Mono.empty().then()
    }

    def size() {
        return repository.size()
    }

    def clear() {
        repository.clear()
    }
}
