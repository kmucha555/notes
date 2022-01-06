package com.mkjb.notes.domain.model;

public final class NoteUser {
    private final String email;
    private final UserRole role;

    public NoteUser(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }

    public String email() {
        return email;
    }

    public UserRole role() {
        return role;
    }

}
