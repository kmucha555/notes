package com.mkjb.notes.shared.dto;


import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.util.context.Context;
import reactor.util.context.ContextView;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.StringJoiner;

@Introspected
public class RequestContext {

    @Header(defaultValue = "")
    @Parameter(required = false, description = "If not provided will be generated")
    private final String requestId;

    @Positive
    @QueryValue(defaultValue = "1")
    @Parameter(required = false)
    private final int pageNumber;

    @Positive
    @QueryValue(defaultValue = "20")
    @Parameter(required = false)
    private final int pageSize;

    @QueryValue(defaultValue = "title:ASC")
    @Parameter(required = false, description = """
            List of properties used to sort the results, separated by commas.
            Possible values:
            * `{fieldName}:ASC`
            * `{fieldName}:DESC`
            """)
    private final String sort;

    @Creator
    RequestContext(final String requestId, final int pageNumber, final int pageSize, final String sort) {
        this.requestId = requestId.isBlank() ? String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) : requestId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public ContextView reactorContext() {
        return Context.of(RequestContext.class, this);
    }

    public String getRequestId() {
        return requestId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSort() {
        return sort;
    }

    public Tuple2<SortFieldName, SortOrder> sortQuery() {
        final var splittedSortQuery = sort.split(":");
        final var sortFieldName = SortFieldName.of(splittedSortQuery[0]);
        final var sortOrder = SortOrder.of(splittedSortQuery[1]);

        return Tuples.of(sortFieldName, sortOrder);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RequestContext.class.getSimpleName() + "[", "]")
                .add("requestId='" + requestId + "'")
                .toString();
    }
}
