package com.mkjb.notes.adapters.api.command;

import com.mkjb.notes.shared.dto.RequestContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            tags = "Notes",
            summary = "Creating a new note for a user",
            description = "Creates a new note for a user",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<NoteIdResponse> createNote(Authentication authentication, @Valid @Body NoteRequest noteRequest,
                                           @RequestBean RequestContext context) {
        return noteService
                .createNote(authentication, noteRequest)
                .contextWrite(context.reactorContext());
    }

    @Put("/{noteId}")
    @Status(HttpStatus.NO_CONTENT)
    @Operation(
            tags = "Notes",
            summary = "Updating a user's note",
            description = "Updates a user's note",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<MutableHttpResponse<Object>> updateNote(Authentication authentication, @PathVariable String noteId,
                                                        @Valid @Body NoteRequest noteRequest, @RequestBean RequestContext context) {
        return noteService
                .updateNote(authentication, noteId, noteRequest)
                .thenReturn(HttpResponse.noContent())
                .contextWrite(context.reactorContext());
    }

    @Delete("/{noteId}")
    @Status(HttpStatus.NO_CONTENT)
    @Operation(
            tags = "Notes",
            summary = "Deleting a user's note",
            description = "Deletes a user's note",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<MutableHttpResponse<Object>> deleteNote(Authentication authentication, @PathVariable String noteId,
                                                        @RequestBean RequestContext context) {
        return noteService
                .deleteNote(authentication, noteId)
                .thenReturn(HttpResponse.noContent())
                .contextWrite(context.reactorContext());
    }

    @Delete
    @Status(HttpStatus.NO_CONTENT)
    @Operation(
            tags = "Notes",
            summary = "Deleting all user's notes",
            description = "Deletes all user's notes",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<MutableHttpResponse<Object>> deleteNotes(Authentication authentication, @RequestBean RequestContext context) {
        return noteService
                .deleteNotes(authentication)
                .then(Mono.just(HttpResponse.noContent()))
                .contextWrite(context.reactorContext());
    }

    @Post("/{noteId}/invitations")
    @Status(HttpStatus.ACCEPTED)
    @Operation(
            tags = "Invitations",
            summary = "Sharing a note with other user",
            description = "Shares a note with other user",
            security = {
                    @SecurityRequirement(name = "OIDC")
            }
    )
    public Mono<MutableHttpResponse<Object>> inviteUserToNote(Authentication authentication, @PathVariable String noteId,
                                                              @Valid @Body InviteRequest inviteRequest, @RequestBean RequestContext context) {
        return noteService
                .invite(authentication, noteId, inviteRequest)
                .then(Mono.just(HttpResponse.accepted()))
                .contextWrite(context.reactorContext());
    }

}
