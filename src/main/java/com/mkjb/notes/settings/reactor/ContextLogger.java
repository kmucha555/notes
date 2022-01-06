package com.mkjb.notes.settings.reactor;

import com.mkjb.notes.settings.time.RequestContext;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.function.Consumer;

public final class ContextLogger {

    private ContextLogger() {
    }

    public static <T> Consumer<Signal<T>> logWithCtx(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) {
                return;
            }

            final var contextView = signal.getContextView().get(RequestContext.class);

            try (MDC.MDCCloseable requestId = MDC.putCloseable("request-id", contextView.getRequestId())) {
                logStatement.accept(signal.get());
            }
        };
    }
}
