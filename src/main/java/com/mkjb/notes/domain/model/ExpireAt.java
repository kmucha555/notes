package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class ExpireAt {
    private final Instant expiredAt;

    public ExpireAt(final Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Instant value() {
        return expiredAt;
    }

}
