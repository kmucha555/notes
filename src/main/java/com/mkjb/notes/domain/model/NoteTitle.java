package com.mkjb.notes.domain.model;

public final class NoteTitle {

    private final String title;

    private NoteTitle(final String title) {
        this.title = title;
    }

    public static NoteTitle of(String title) {
        return new NoteTitle(title);
    }

    public String value() {
        return title;
    }
}
