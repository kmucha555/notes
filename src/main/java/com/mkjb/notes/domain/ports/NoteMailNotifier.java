package com.mkjb.notes.domain.ports;

import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.domain.model.NoteUser;
import reactor.core.publisher.Mono;

public interface NoteMailNotifier {

    Mono<Void> sendNotification(NoteId noteId, NoteUser noteUser);
}
