package com.mkjb.notes.domain;

import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.model.NoteUser;
import com.mkjb.notes.domain.ports.NoteMailNotifier;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class NotePublisher {

    private final NoteMailNotifier mailNotifier;

    NotePublisher(final NoteMailNotifier mailNotifier) {
        this.mailNotifier = mailNotifier;
    }

    public Mono<Void> notify(final NoteId note, final NoteUser noteUserToAdd) {
        return mailNotifier.sendNotification(note, noteUserToAdd);
    }
}
