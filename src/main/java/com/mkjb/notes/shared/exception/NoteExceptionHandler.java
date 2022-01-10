package com.mkjb.notes.shared.exception;


import com.mkjb.notes.settings.error.ErrorResponse;
import com.mkjb.notes.shared.dto.RequestContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Produces
@Singleton
@Requires(classes = {NoteException.class, ExceptionHandler.class})
class NoteExceptionHandler implements ExceptionHandler<NoteException, HttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(NoteExceptionHandler.class);

    @Override
    public HttpResponse handle(HttpRequest request, NoteException exception) {
        final var context = exception.getContext().get(RequestContext.class);

        try (MDC.MDCCloseable requestId = MDC.putCloseable("request-id", context.getRequestId())) {
            log.error("Handling NoteException. Response status '{}' Cause: '{}' Note Id: '{}'", exception.getStatus(), exception.getMessage(), exception.getNoteId());
        }

        final var errorResponse = new ErrorResponse(
                exception.getNoteId().value(),
                exception.getStatus().getReason(),
                exception.getMessage()
        );

        return HttpResponse.status(exception.getStatus()).body(errorResponse);
    }
}
