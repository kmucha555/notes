package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.NoteUser;

public final class UserDocument {

    private String email;
    private String role;

    public UserDocument() {
    }

    private UserDocument(final String email, final String role) {
        this.email = email;
        this.role = role;
    }

    static UserDocument of(final NoteUser noteUser) {
        return new UserDocument(noteUser.email().value(), noteUser.role().name());
    }

    NoteUser toDomain() {
        return NoteUser.of(email, role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }
}
