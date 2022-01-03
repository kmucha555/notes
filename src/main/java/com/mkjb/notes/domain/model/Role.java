package com.mkjb.notes.domain.model;

import io.micronaut.core.annotation.Introspected;

@Introspected
public enum Role {
    OWNER, VIEWER, EDITOR
}
