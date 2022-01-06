package com.mkjb.notes.settings.time;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.validation.validator.DefaultClockProvider;
import jakarta.inject.Singleton;

import javax.validation.ClockProvider;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Singleton
@Replaces(DefaultClockProvider.class)
@Requires(env = "test")
class TestTimeProvider implements ClockProvider {

    @Override
    public Clock getClock() {
        return Clock.fixed(Instant.parse("2022-06-01T21:00:00Z"), ZoneOffset.UTC);
    }
}
