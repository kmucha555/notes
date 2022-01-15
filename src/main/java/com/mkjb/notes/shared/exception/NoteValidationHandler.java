package com.mkjb.notes.shared.exception;


import com.mkjb.notes.settings.error.ErrorResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {NoteValidationException.class, ExceptionHandler.class})
class NoteValidationHandler implements ExceptionHandler<NoteValidationException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, NoteValidationException exception) {

        final var errorResponse = new ErrorResponse(
                null,
                exception.getStatus().getReason(),
                exception.getMessage()
        );

        return HttpResponse.status(exception.getStatus()).body(errorResponse);
    }
}
