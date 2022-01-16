package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.domain.model.NoteUser;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Introspected
record InviteRequest(@Email String email, @NotBlank String role) {

    NoteUser toDomain() {
        return NoteUser.of(email, role);
    }
}
