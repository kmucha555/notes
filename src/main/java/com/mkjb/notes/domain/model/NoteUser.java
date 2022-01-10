package com.mkjb.notes.domain.model;

public final class NoteUser {
    private final String email;
    private final UserRole role;

    private NoteUser(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }

    public static NoteUser of(String email, UserRole role) {
        return new NoteUser(email, role);
    }

    public String email() {
        return email;
    }

    public UserRole role() {
        return role;
    }

}
