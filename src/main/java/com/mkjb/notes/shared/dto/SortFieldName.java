package com.mkjb.notes.shared.dto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public enum SortFieldName {
    TITLE("title"), METADATA_CREATEDAT("metadata.createdAt");

    private final String fieldName;

    SortFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public String value() {
        return fieldName;
    }

    public static SortFieldName of(final String fieldName) {
        final String parsedFieldName = fieldName.toUpperCase().replaceAll("\\.", "_");

        return SortFieldName.valueOf(parsedFieldName);
    }
}