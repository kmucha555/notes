package com.mkjb.notes.adapters.mongo;

public class UserDocument {

    private String email;
    private String role;

    public UserDocument(final String email, final String role) {
        this.email = email;
        this.role = role;
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
