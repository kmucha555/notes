package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;

import java.util.Objects;

public final class NoteUser {

    private final NoteUserEmail email;
    private final NoteUserRole userRole;

    private NoteUser(NoteUserEmail email, final NoteUserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public static NoteUser of(final NoteUserEmail noteUserEmail, final NoteUserRole role) {
        return new NoteUser(noteUserEmail, role);
    }

    public static NoteUser of(final String email, final String role) {
        return new NoteUser(NoteUserEmail.of(email), parseRole(role));
    }

    public NoteUserEmail email() {
        return email;
    }

    public NoteUserRole role() {
        return userRole;
    }


    private static NoteUserRole parseRole(final String role) {
        try {
            return NoteUserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw NoteValidationException.NOTE_USER_ROLE_NOT_EXISTS;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NoteUser noteUser = (NoteUser) o;
        return Objects.equals(email, noteUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
