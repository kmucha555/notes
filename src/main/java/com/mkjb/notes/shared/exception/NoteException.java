package com.mkjb.notes.shared.exception;

import com.mkjb.notes.domain.model.NoteId;
import io.micronaut.http.HttpStatus;
import reactor.util.context.ContextView;

public class NoteException extends RuntimeException {

    private final ContextView context;
    private final NoteId noteId;
    private final HttpStatus status;
    private final String message;

    public NoteException(final ContextView context, final NoteId noteId, final HttpStatus status, final String message) {
        super();
        this.context = context;
        this.noteId = noteId;
        this.status = status;
        this.message = message;
    }

    public static NoteException notFound(final ContextView context, final NoteId noteId) {
        return new NoteException(context, noteId, HttpStatus.NOT_FOUND, "Note with given id not found");
    }

    public static NoteException internal(final ContextView context, final NoteId noteId, final String message) {
        return new NoteException(context, noteId, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static NoteException internal(final ContextView context, final String message) {
        return new NoteException(context, null, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ContextView getContext() {
        return context;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
