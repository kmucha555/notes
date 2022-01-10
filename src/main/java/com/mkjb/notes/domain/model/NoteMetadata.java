package com.mkjb.notes.domain.model;

import java.util.Optional;

public final class NoteMetadata {
    private final ExpireAt expireAt;
    private final CreatedAt createdAt;
    private final ModifiedAt modifiedAt;

    private NoteMetadata(final ExpireAt expireAt, final CreatedAt createdAt, final ModifiedAt modifiedAt) {
        this.expireAt = expireAt;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static NoteMetadata of(final ExpireAt expireAt) {
        return new NoteMetadata(expireAt, null, null);
    }

    public static NoteMetadata of(final ExpireAt expireAt, final CreatedAt createdAt) {
        return new NoteMetadata(expireAt, createdAt, null);
    }

    public static NoteMetadata of(final ExpireAt expireAt, final CreatedAt createdAt, final ModifiedAt modifiedAt) {
        return new NoteMetadata(expireAt, createdAt, modifiedAt);
    }

    public Optional<ExpireAt> expireAt() {
        return Optional.ofNullable(expireAt);
    }

    public Optional<CreatedAt> createdAt() {
        return Optional.ofNullable(createdAt);
    }

    public Optional<ModifiedAt> modifiedAt() {
        return Optional.ofNullable(modifiedAt);
    }

}
