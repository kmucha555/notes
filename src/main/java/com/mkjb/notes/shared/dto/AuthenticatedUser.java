package com.mkjb.notes.shared.dto;

import com.mkjb.notes.shared.exception.NoteValidationException;
import io.micronaut.security.authentication.Authentication;

import java.util.Objects;

public record AuthenticatedUser(String email) {

    public static AuthenticatedUser of(final Authentication authentication) {
        final var email = String.valueOf(authentication.getAttributes().get("email"));

        if (Objects.isNull(email) || email.isBlank()) {
            throw NoteValidationException.USER_NOT_AUTHENTICATED;
        }

        return new AuthenticatedUser(email);
    }
}
