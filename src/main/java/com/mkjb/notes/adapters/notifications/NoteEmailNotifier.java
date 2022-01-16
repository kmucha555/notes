package com.mkjb.notes.adapters.notifications;

import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.model.NoteUser;
import com.mkjb.notes.domain.ports.NoteMailNotifier;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Singleton
class NoteEmailNotifier implements NoteMailNotifier {

    private static final Logger log = LoggerFactory.getLogger(NoteEmailNotifier.class);
    @Override
    public Mono<Void> sendNotification(final NoteId noteId, final NoteUser noteUser) {
        log.info("Notification request received for note id '{}'", noteId);
        /**
         * Mail system integration implementation comes here
         */
        return Mono.empty().then();
    }
}
