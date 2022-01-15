package com.mkjb.notes.adapters.api.command

import com.mkjb.notes.domain.model.*
import com.mkjb.notes.domain.ports.InMemoryNoteCommandRepository
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class NoteServiceSpec extends Specification {

    def 'aaa'() {
        given:
        def repository = new InMemoryNoteCommandRepository()
        def clock = Clock.fixed(Instant.parse("2022-06-01T21:00:00Z"), ZoneOffset.UTC)
        def noteService = new NoteService(repository)

        def user = new NoteRequest.User('john.doe@supernote.com', 'ADMIN')
        new NoteRequest(
                'Test Note',
                'Lorem ipsum',
                Set.of(user),
                Instant.now(clock),

        )
        def note = Note
                .note()
                .withTitle(NoteTitle.of("Test Note"))
                .withContent(NoteContent.of("Lorem ipsum "))
//                .withUsers([["email": "kmucha555@gmail.com", "role": "OWNER"]])
                .withMetadata(NoteMetadata.of(null))
                .withVersion(NoteVersion.of(0))
                .build()

        when:
        def noteId = noteService.createNote(note).block()

        then:
        noteId.value() != ''
    }

}
