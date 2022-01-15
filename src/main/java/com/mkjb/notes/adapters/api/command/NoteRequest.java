package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Introspected
record NoteRequest(String title, String content,
                   @NotEmpty Set<User> users,
                   @Nullable @Future Instant expireAt,
                   @Min(0) @Max(Integer.MAX_VALUE) int version) {

    Note toDomain() {
        return Note
                .note()
                .withTitle(NoteTitle.of(title))
                .withContent(NoteContent.of(content))
                .withUsers(toDomainUsers())
                .withMetadata(NoteMetadata.of(ExpireAt.of(expireAt)))
                .withVersion(NoteVersion.of(version))
                .build();
    }

    private List<NoteUser> toDomainUsers() {
        return users.stream().map(User::toDomain).toList();
    }

    @Introspected
    record User(@Email String email, @NotBlank String role) {

        NoteUser toDomain() {
            return NoteUser.of(email, role);
        }

    }

}
