package com.mkjb.notes.domain.model;

import java.time.Instant;
import java.util.Optional;

public final class NoteExpireAt {

    private final Instant expireAt;

    private NoteExpireAt(final Instant expireAt) {
        this.expireAt = expireAt;
    }

    public static NoteExpireAt of(final Instant expireAt) {
        return new NoteExpireAt(expireAt);
    }

    public Optional<Instant> value() {
        return Optional.ofNullable(expireAt);
    }

    public Instant getOrNull() {
        return expireAt;
    }
}
