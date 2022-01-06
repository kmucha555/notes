package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.settings.time.RequestContext;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.RequestBean;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller("/notes")
class NoteCommandController {

    private final NoteService noteService;

    NoteCommandController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @Post
    public Mono<NoteIdResponse> createNote(@RequestBean RequestContext context, @Valid @Body NoteRequest noteRequest) {
        return noteService
                .createNote(noteRequest)
                .contextWrite(context.reactorContext())
                .map(NoteIdResponse::of);
    }

}
