package com.mkjb.notes.domain.model;

import java.util.Optional;

public final class Metadata {
    private final ExpireAt expiredAt;
    private final CreatedAt createdAt;
    private final ModifiedAt modifiedAt;

    public Metadata(final ExpireAt expiredAt, final CreatedAt createdAt, final ModifiedAt modifiedAt) {
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static Metadata of(final ExpireAt expireAt) {
        return new Metadata(expireAt, null, null);
    }

    public static Metadata of(final ExpireAt expireAt, final CreatedAt createdAt) {
        return new Metadata(expireAt, createdAt, null);
    }

    public static Metadata of(final ExpireAt expireAt, final CreatedAt createdAt, final ModifiedAt modifiedAt) {
        return new Metadata(expireAt, createdAt, modifiedAt);
    }

    public Optional<ExpireAt> expireAt() {
        return Optional.ofNullable(expiredAt);
    }

    public Optional<CreatedAt> createdAt() {
        return Optional.ofNullable(createdAt);
    }

    public Optional<ModifiedAt> modifiedAt() {
        return Optional.ofNullable(modifiedAt);
    }

}
