package com.mkjb.notes.adapters.notifications;

import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.model.NoteUser;
import com.mkjb.notes.domain.ports.NoteMailNotifier;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
class NoteEmailNotifier implements NoteMailNotifier {
    @Override
    public Mono<Void> sendNotification(final NoteId noteId, final NoteUser noteUser) {
        /**
         * Mail system integration implementation comes here
         */
        return Mono.empty().then();
    }
}
