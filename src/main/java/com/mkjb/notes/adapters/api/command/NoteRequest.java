package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.*;
import java.time.Instant;

@Introspected
record NoteRequest(String title, String content,
                   @Nullable @Future Instant expireAt,
                   @Min(0) @Max(Integer.MAX_VALUE) int version) {

    Note toDomain() {
        return Note
                .note()
                .withTitle(NoteTitle.of(title))
                .withContent(NoteContent.of(content))
                .withMetadata(NoteMetadata.of(NoteExpireAt.of(expireAt)))
                .withVersion(NoteVersion.of(version))
                .build();
    }

}
