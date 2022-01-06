package com.mkjb.notes.settings.time;


import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import javax.validation.constraints.Positive;
import java.util.StringJoiner;
import java.util.UUID;

@Introspected
public class RequestContext  {

    @Header(defaultValue = "")
    private final String requestId;

    @Positive
    @QueryValue(defaultValue = "1")
    private final int pageNumber;

    @Positive
    @QueryValue(defaultValue = "20")
    private final int pageSize;

    @QueryValue(defaultValue = "ASC")
    private final Sort sort;

    @Creator
    RequestContext(final String requestId, final int pageNumber, final int pageSize, final Sort sort) {

        this.requestId = requestId.isBlank() ? UUID.randomUUID().toString() : requestId;
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

    public Sort getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RequestContext.class.getSimpleName() + "[", "]")
                .add("requestId='" + requestId + "'")
                .add("pageNumber=" + pageNumber)
                .add("pageSize=" + pageSize)
                .add("sort=" + sort)
                .toString();
    }
}
