package com.mkjb.notes.domain.model;

import io.micronaut.core.annotation.Introspected;

import javax.management.relation.Role;

@Introspected
public record User(String email, Role role) {
}
