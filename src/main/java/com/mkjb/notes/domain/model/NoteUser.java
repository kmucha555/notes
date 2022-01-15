package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;

public final class NoteUser {

    private final String email;
    private final UserRole role;

    private NoteUser(String email, String role) {
        validateRole(role);

        this.email = email;
        this.role = UserRole.valueOf(role);
    }

    private void validateRole(final String role) {
        try {
            UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw NoteValidationException.NOTE_USER_ROLE_NOT_EXISTS;
        }
    }

    public static NoteUser of(String email, String role) {
        return new NoteUser(email, role);
    }

    public String emailValue() {
        return email;
    }

    public UserRole roleValue() {
        return role;
    }

}
