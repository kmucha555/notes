package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.shared.dto.RequestContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/notes")
class NoteCommandController {

    private final NoteService noteService;

    NoteCommandController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @Post
    public Mono<NoteIdResponse> createNote(Authentication authentication, @Valid @Body NoteRequest noteRequest, @RequestBean RequestContext context) {
        return noteService
                .createNote(authentication, noteRequest)
                .contextWrite(context.reactorContext());
    }

    @Put("/{noteId}")
    public Mono<MutableHttpResponse<Object>> updateNote(@PathVariable String noteId, @Valid @Body NoteRequest noteRequest, @RequestBean RequestContext context) {
        return noteService
                .updateNote(noteId, noteRequest)
                .thenReturn(HttpResponse.noContent())
                .contextWrite(context.reactorContext());
    }

    @Delete("/{noteId}")
    public Mono<MutableHttpResponse<Object>> deleteNote(@PathVariable String noteId, @RequestBean RequestContext context) {
        return noteService
                .deleteNote(noteId)
                .thenReturn(HttpResponse.noContent())
                .contextWrite(context.reactorContext());
    }

    @Delete
    public Mono<MutableHttpResponse<Object>> deleteNotes(@RequestBean RequestContext context) {
        return noteService
                .deleteNotes()
                .thenReturn(HttpResponse.noContent())
                .contextWrite(context.reactorContext());
    }

}
