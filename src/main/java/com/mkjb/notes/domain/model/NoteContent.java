package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;

import java.util.Objects;

public final class NoteContent {

    private static final int MAX_CONTENT_LENGTH = 2048;

    private final String content;

    private NoteContent(String content) {
        validateNoteContentIsBlank(content);
        validateNoteContentLength(content);

        this.content = content;
    }

    public static NoteContent of(final String content) {
        return new NoteContent(content);
    }

    public String value() {
        return content;
    }

    private void validateNoteContentIsBlank(final String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw NoteValidationException.NOTE_CONTENT_BLANK;
        }
    }

    private void validateNoteContentLength(final String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw NoteValidationException.NOTE_CONTENT_TOO_LONG;
        }
    }

}
