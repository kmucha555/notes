package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;
import org.bson.types.ObjectId;

import java.util.Objects;

public final class NoteId {

    private final String id;

    private NoteId(String id) {
        validateNoteIdIsBlank(id);
        validateNoteIdIsParsableToObjectId(id);

        this.id = id;
    }

    public static NoteId of(final String id) {
        return new NoteId(id);
    }

    private void validateNoteIdIsBlank(final String noteId) {
        if (Objects.isNull(noteId) || noteId.isBlank()) {
            throw NoteValidationException.NOTE_ID_BLANK;
        }
    }

    private void validateNoteIdIsParsableToObjectId(final String noteId) {
        if (!ObjectId.isValid(noteId)) {
            throw NoteValidationException.NOTE_ID_WRONG_FORMAT;
        }
    }

    public String value() {
        return id;
    }

    public ObjectId toObjectId() {
        return new ObjectId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (NoteId) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }

}
