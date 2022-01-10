package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.*;
import io.micronaut.core.annotation.Introspected;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Introspected
record NoteResponse(String id, String title, String content, List<UserResponse> users,
                    int version, MetadataResponse metadata) {

    static NoteResponse of(final Note note) {
        return new NoteResponse(
                note.getId().value(),
                note.getTitle().value(),
                note.getContent().value(),
                toUsersResponse(note.getUsers()),
                note.getVersion().value(),
                MetadataResponse.of(note.getMetadata())
        );
    }

    static List<UserResponse> toUsersResponse(final Set<NoteUser> users) {
        return users
                .stream()
                .map(UserResponse::of)
                .toList();
    }

    @Introspected
    record UserResponse(String email, String role) {

        static UserResponse of(final NoteUser noteUser) {
            return new UserResponse(noteUser.email(), noteUser.role().name());
        }
    }

    @Introspected
    record MetadataResponse(Instant createdAt, Instant modifiedAt, Instant expireAt) {

        static MetadataResponse of(final NoteMetadata metadata) {
            return new MetadataResponse(
                    metadata.createdAt().map(CreatedAt::value).orElse(null),
                    metadata.modifiedAt().map(ModifiedAt::value).orElse(null),
                    metadata.expireAt().map(ExpireAt::value).orElse(null)
            );
        }
    }
}
