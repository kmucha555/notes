package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class ExpireAt {

    private final Instant expireAt;

    private ExpireAt(final Instant expireAt) {
        this.expireAt = expireAt;
    }

    public static ExpireAt of(final Instant expireAt) {
        return new ExpireAt(expireAt);
    }

    public Instant value() {
        return expireAt;
    }

}
