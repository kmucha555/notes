package com.mkjb.notes.domain.model;

import io.micronaut.core.annotation.Introspected;

import java.time.Instant;

@Introspected
public record Metadata(Instant createdAt, Instant modifiedAt, Instant expiredAt) {
}
