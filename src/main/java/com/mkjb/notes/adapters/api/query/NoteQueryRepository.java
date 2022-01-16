package com.mkjb.notes.adapters.api.query;

import com.mkjb.notes.adapters.mongo.NoteDocument;
import com.mkjb.notes.domain.model.NoteId;
import com.mkjb.notes.shared.dto.AuthenticatedUser;
import com.mkjb.notes.shared.dto.RequestContext;
import com.mkjb.notes.shared.dto.SortFieldName;
import com.mkjb.notes.shared.dto.SortOrder;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import io.micronaut.security.authentication.Authentication;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static com.mkjb.notes.shared.dto.SortOrder.ASC;

public interface NoteQueryRepository {
    int FIRST_PAGE_INDEX = 1;

    Mono<NoteDocument> findById(final Authentication authentication, NoteId noteId);

    Flux<NoteDocument> findAll(final Authentication authentication);

    default Bson findByNoteId(final NoteId noteId) {
        return Filters.eq(noteId.toObjectId());
    }

    default Bson findByUserEmailId(final AuthenticatedUser authenticatedUser) {
        return Filters.eq("users.email", authenticatedUser.email());
    }

    default int pageNumber(final RequestContext context) {
        return calculateCurrentPageIndex(context);
    }

    default int pageSize(final RequestContext context) {
        return context.getPageSize();
    }

    default Bson sort(final RequestContext context) {
        final Tuple2<SortFieldName, SortOrder> sortQuery = context.sortQuery();
        final SortOrder sortOrder = sortQuery.getT2();
        final SortFieldName fieldName = sortQuery.getT1();

        return sortOrder.equals(ASC) ? Sorts.ascending(fieldName.value()) : Sorts.descending(fieldName.value());
    }

    private int calculateCurrentPageIndex(final RequestContext context) {
        return context.getPageSize() * (context.getPageNumber() - FIRST_PAGE_INDEX);
    }

}
