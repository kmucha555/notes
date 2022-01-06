package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class CreatedAt {

    private final Instant createdAt;

    public CreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant value() {
        return createdAt;
    }

}
