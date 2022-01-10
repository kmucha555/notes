package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Introspected
record NoteRequest(@Nullable String title, @NotBlank String content, @NotEmpty Set<User> users,
                   @Nullable @Future Instant expireAt, @Min(0) @Max(Integer.MAX_VALUE) int version) {

    Note toDomain() {
        final Set<NoteUser> domainUsers = toDomainUsers();

        return Note
                .note()
                .withTitle(NoteTitle.of(title))
                .withContent(NoteContent.of(content))
                .withUsers(domainUsers)
                .withMetadata(NoteMetadata.of(ExpireAt.of(expireAt)))
                .withVersion(NoteVersion.of(version))
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
            return NoteUser.of(email, UserRole.valueOf(role));
        }

    }

}
