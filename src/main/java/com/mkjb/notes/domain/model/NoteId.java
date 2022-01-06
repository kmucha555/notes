package com.mkjb.notes.domain.model;

public record NoteId(String id) {

    public static NoteId of(final String id) {
        return new NoteId(id);
    }

    public String value() {
        return id;
    }

}
