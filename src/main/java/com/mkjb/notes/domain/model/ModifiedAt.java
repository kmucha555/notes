package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class ModifiedAt {

    private final Instant modifiedAt;

    private ModifiedAt(final Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public static ModifiedAt of(final Instant modifiedAt) {
        return new ModifiedAt(modifiedAt);
    }

    public Instant value() {
        return modifiedAt;
    }

}
