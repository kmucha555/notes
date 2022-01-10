package com.mkjb.notes.domain.model;

public final class NoteContent {
    private final String content;

    private NoteContent(String content) {
        this.content = content;
    }

    public static NoteContent of(final String content) {
        return new NoteContent(content);
    }

    public String value() {
        return content;
    }

}
