package com.mkjb.notes.domain.model;

public final class NoteMetadata {
    private final ExpireAt expireAt;

    private NoteMetadata(final ExpireAt expireAt) {
        this.expireAt = expireAt;
    }

    public static NoteMetadata of(final ExpireAt expireAt) {
        return new NoteMetadata(expireAt);
    }
    
    public ExpireAt expireAtValue() {
        return expireAt;
    }

}
