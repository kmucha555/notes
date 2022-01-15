package com.mkjb.notes.shared.exception;

import io.micronaut.http.HttpStatus;

public class NoteValidationException extends RuntimeException {

    public static final NoteValidationException NOTE_ID_BLANK = badRequest("Note id must not be empty");
    public static final NoteValidationException NOTE_ID_WRONG_FORMAT = badRequest("Note id must be in proper format");
    public static final NoteValidationException NOTE_TITLE_BLANK = badRequest("Note title must not be empty");
    public static final NoteValidationException NOTE_TITLE_TOO_LONG = badRequest("Note title too long");
    public static final NoteValidationException NOTE_CONTENT_BLANK = badRequest("Note content must not be empty");
    public static final NoteValidationException NOTE_CONTENT_TOO_LONG = badRequest("Note content too long");

    private final HttpStatus status;
    private final String message;

    private NoteValidationException(final HttpStatus status, final String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public static NoteValidationException badRequest(final String message) {
        return new NoteValidationException(HttpStatus.BAD_REQUEST, message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
