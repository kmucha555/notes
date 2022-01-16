package com.mkjb.notes.domain.model;

import java.util.Objects;

public final class NoteUserEmail {

    private final String email;

    private NoteUserEmail(final String email) {
        this.email = email;
    }

    public static NoteUserEmail of(final String email) {
        return new NoteUserEmail(email);
    }

    public String value() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NoteUserEmail that = (NoteUserEmail) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
