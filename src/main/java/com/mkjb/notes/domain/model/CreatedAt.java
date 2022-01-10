package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class CreatedAt {

    private final Instant createdAt;

    private CreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static CreatedAt of(final Instant createdAt) {
        return new CreatedAt(createdAt);
    }

    public Instant value() {
        return createdAt;
    }

}
