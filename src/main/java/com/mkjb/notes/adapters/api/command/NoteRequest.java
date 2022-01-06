package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Introspected
record NoteRequest(@Nullable String title, @NotBlank String content, @NotEmpty Set<User> users,
                   @Nullable @Future Instant expireAt) {

    Note toDomain() {
        final Set<NoteUser> domainUsers = toDomainUsers();

        return Note
                .note()
                .title(NoteTitle.of(title))
                .withContent(NoteContent.of(content))
                .withUsers(domainUsers)
                .withMetadata(Metadata.of(expireAt))
                .build();
    }

    private Set<NoteUser> toDomainUsers() {
        return users
                .stream()
                .map(User::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Introspected
    record User(@Email String email, @NotBlank String role) {

        NoteUser toDomain() {
            return new NoteUser(email, UserRole.valueOf(role));
        }
    }
}
