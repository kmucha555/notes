package com.mkjb.notes.domain.model;

import com.mkjb.notes.shared.exception.NoteValidationException;

import java.util.List;

public final class NoteUser {

    private final String email;
    private final UserRole role;

    private NoteUser(String email, String role) {
        this.email = email;
        this.role = parseRole(role);
    }

    public static NoteUser of(String email, String role) {
        return new NoteUser(email, role);
    }

    public static void validateExactlyOneUserInOwnerRole(final List<NoteUser> users) {
        final long numberOfNoteOwners = users.stream().map(NoteUser::roleValue).filter(userRole -> userRole == UserRole.OWNER).count();

        if (numberOfNoteOwners > 1) {
            throw NoteValidationException.TOO_MANY_OWNERS;
        }

        if (numberOfNoteOwners < 1) {
            throw NoteValidationException.TOO_FEW_OWNERS;
        }
    }

    public String emailValue() {
        return email;
    }

    public UserRole roleValue() {
        return role;
    }

    private UserRole parseRole(final String role) {
        try {
            return UserRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw NoteValidationException.NOTE_USER_ROLE_NOT_EXISTS;
        }
    }

}
