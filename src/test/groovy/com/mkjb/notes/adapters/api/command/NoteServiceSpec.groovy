package com.mkjb.notes.adapters.api.command

import com.mkjb.notes.domain.model.NoteId
import com.mkjb.notes.domain.ports.InMemoryNoteCommandRepository
import com.mkjb.notes.domain.ports.NoteFacade
import spock.lang.Specification

import java.time.Instant

class NoteServiceSpec extends Specification {

    def 'aaa'() {
        given:
        def repository = new InMemoryNoteCommandRepository()
        def noteFacade = new NoteFacade(repository)
        def expireAt = Instant.parse("2023-06-01T21:00:00Z")
        def noteService = new NoteService(noteFacade)

        def user = new NoteRequest.User('john.doe@supernote.com', 'OWNER')
        def noteRequest = new NoteRequest('Test Note', 'Lorem ipsum', Set.of(user), expireAt, 0)

        when:
        def noteId = noteService.createNote(noteRequest).block().id()

        then:
        def actualNote = repository.getById(NoteId.of(noteId))

        verifyAll {
            actualNote.title.value() == 'Test Note'
            actualNote.content.value() == 'Lorem ipsum'
            actualNote.metadata.expireAtValue().value().get() == Instant.parse("2023-06-01T21:00:00Z")
            actualNote.version.value() == 0
        }
    }

}
