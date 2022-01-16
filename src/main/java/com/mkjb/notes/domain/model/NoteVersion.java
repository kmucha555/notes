package com.mkjb.notes.domain.model;

public final class NoteVersion {

    public static final NoteVersion INITIAL_VERSION = new NoteVersion(0);

    private final int version;

    private NoteVersion(int version) {
        this.version = version;
    }

    public static NoteVersion of(final int version) {
        return new NoteVersion(version);
    }

    public int value() {
        return version;
    }

    public int increment() {
        return version + 1;
    }

}
