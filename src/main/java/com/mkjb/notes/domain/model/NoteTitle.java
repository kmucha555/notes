package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;

import java.util.Objects;

public final class NoteTitle {

    private static final int MAX_TITLE_LENGTH = 256;

    private final String title;

    private NoteTitle(final String title) {
        validateNoteTitleIsBlank(title);
        validateNoteTitleLength(title);

        this.title = title;
    }

    public static NoteTitle of(String title) {
        return new NoteTitle(title);
    }

    private void validateNoteTitleIsBlank(final String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            throw NoteValidationException.NOTE_TITLE_BLANK;
        }
    }

    private void validateNoteTitleLength(final String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw NoteValidationException.NOTE_TITLE_TOO_LONG;
        }
    }

    public String value() {
        return title;
    }
}
