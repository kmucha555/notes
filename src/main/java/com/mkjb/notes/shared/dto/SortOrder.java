package com.mkjb.notes.shared.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public enum SortOrder {
    ASC, DESC;

    public static SortOrder of(final String sortOrder) {
        return SortOrder.valueOf(sortOrder.toUpperCase());
    }

}