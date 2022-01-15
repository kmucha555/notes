package com.mkjb.notes.settings.security;

import io.micronaut.core.annotation.NonNull;

public interface ApplicationConfiguration {

    @NonNull
    String getHostedDomain();
}
