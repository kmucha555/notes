package com.mkjb.notes.domain.model;

public final class NoteMetadata {
    private final NoteExpireAt expireAt;

    private NoteMetadata(final NoteExpireAt expireAt) {
        this.expireAt = expireAt;
    }

    public static NoteMetadata of(final NoteExpireAt expireAt) {
        return new NoteMetadata(expireAt);
    }
    
    public NoteExpireAt expireAtValue() {
        return expireAt;
    }

}
