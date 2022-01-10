package com.mkjb.notes.settings.error;

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;

@Singleton
@Primary
class CustomErrorResponseProcessor implements ErrorResponseProcessor<ErrorResponse> {

    @Override
    @NonNull
    public MutableHttpResponse<ErrorResponse> processResponse(@NonNull ErrorContext errorContext, @NonNull MutableHttpResponse<?> response) {
        final var path = errorContext.getRequest().getPath();
        final var message = errorContext.getRootCause().map(Throwable::getMessage).orElse("Error occurred");
        final var status = response.getStatus().getReason();

        final var errorResponse = new ErrorResponse(
                path,
                status,
                message
        );

        return response.body(errorResponse).contentType(MediaType.APPLICATION_JSON_TYPE);
    }
}
