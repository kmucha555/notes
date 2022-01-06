package com.mkjb.notes.domain.model;

public class NoteTitle {

    private final String title;

    public NoteTitle(final String title) {
        this.title = title;
    }

    public static NoteTitle of(String title) {
        return new NoteTitle(title);
    }

    public String value() {
        return title;
    }
}
