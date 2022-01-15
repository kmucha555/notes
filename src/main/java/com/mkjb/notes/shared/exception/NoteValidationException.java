package com.mkjb.notes.shared.exception;

import com.mkjb.notes.domain.model.UserRole;
import io.micronaut.http.HttpStatus;

import java.util.Arrays;

public class NoteValidationException extends RuntimeException {

    public static final NoteValidationException NOTE_ID_BLANK = badRequest("The note id must not be empty");
    public static final NoteValidationException NOTE_ID_WRONG_FORMAT = badRequest("The note id must be in proper format");
    public static final NoteValidationException NOTE_TITLE_BLANK = badRequest("The note title must not be empty");
    public static final NoteValidationException NOTE_TITLE_TOO_LONG = badRequest("The note title too long");
    public static final NoteValidationException NOTE_CONTENT_BLANK = badRequest("The note content must not be empty");
    public static final NoteValidationException NOTE_CONTENT_TOO_LONG = badRequest("The note content too long");
    public static final NoteValidationException NOTE_USER_ROLE_NOT_EXISTS = badRequest("Provided user role is not supported Supported roles %s".formatted(Arrays.stream(UserRole.values()).toList()));
    public static final NoteValidationException TOO_MANY_OWNERS = badRequest("The note must have only one user in OWNER role. You provided few of them.");
    public static final NoteValidationException TOO_FEW_OWNERS = badRequest("The note must have exactly only one user in OWNER role. Please provide valid user details");

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
