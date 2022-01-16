package com.mkjb.notes.adapters.notifications

import com.mkjb.notes.domain.model.NoteId
import com.mkjb.notes.domain.model.NoteUser
import com.mkjb.notes.domain.ports.NoteMailNotifier
import reactor.core.publisher.Mono

import java.util.concurrent.ConcurrentHashMap

class InMemoryEmailNotifier implements NoteMailNotifier {
    private ConcurrentHashMap<NoteId, NoteUser> repository = new ConcurrentHashMap<>();


    @Override
    Mono<Void> sendNotification(final NoteId noteId, final NoteUser noteUser) {
        repository.put(noteId, noteUser)
        return Mono.empty().then()
    }

    def get(NoteId noteId) {
        return repository.get(noteId)
    }
}
