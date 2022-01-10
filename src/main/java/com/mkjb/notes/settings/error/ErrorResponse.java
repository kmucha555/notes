package com.mkjb.notes.settings.error;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record ErrorResponse(String noteId, String status, String message) {
}
