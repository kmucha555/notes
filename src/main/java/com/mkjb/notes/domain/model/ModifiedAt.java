package com.mkjb.notes.domain.model;

import java.time.Instant;

public final class ModifiedAt {

    private final Instant modifiedAt;

    public ModifiedAt(final Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Instant value() {
        return modifiedAt;
    }

}
