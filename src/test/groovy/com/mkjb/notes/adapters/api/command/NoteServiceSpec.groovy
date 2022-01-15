package com.mkjb.notes.adapters.api.command

import com.mkjb.notes.domain.model.NoteId
import com.mkjb.notes.domain.ports.InMemoryNoteCommandRepository
import com.mkjb.notes.domain.ports.NoteFacade
import spock.lang.Specification

import java.time.Instant

class NoteServiceSpec extends Specification implements NoteCommandTestData {

    def repository = new InMemoryNoteCommandRepository()
    def noteFacade = new NoteFacade(repository)
    def noteService = new NoteService(noteFacade)

    def 'should create a note'() {
        when:
        def noteId = noteService.createNote(createNoteRequest).block().id()

        then:
        def createdNote = repository.getById(NoteId.of(noteId))

        verifyAll {
            createdNote.title.value() == 'Test Note'
            createdNote.content.value() == 'Lorem ipsum'
            createdNote.users.get(0).emailValue() == 'john.doe@supernote.com'
            createdNote.users.get(0).roleValue().toString() == 'OWNER'
            createdNote.version.value() == 0
            createdNote.metadata.expireAtValue().value().get() == Instant.parse("2023-06-01T21:00:00Z")
        }
    }

    def 'should thrown exception when user is not in OWNER role create a note'() {
        when:
        def noteId = noteService.createNote(createNoteRequest).block().id()

        then:
        def createdNote = repository.getById(NoteId.of(noteId))

        verifyAll {
            createdNote.title.value() == 'Test Note'
            createdNote.content.value() == 'Lorem ipsum'
            createdNote.users.get(0).emailValue() == 'john.doe@supernote.com'
            createdNote.users.get(0).roleValue().toString() == 'OWNER'
            createdNote.version.value() == 0
            createdNote.metadata.expireAtValue().value().get() == Instant.parse("2023-06-01T21:00:00Z")
        }
    }

    def 'should update a note'() {
        given:
        def noteId = noteService.createNote(createNoteRequest).block().id()

        when:
        noteService.updateNote(noteId, updateNoteRequest).block()

        then:
        def updatedNote = repository.getById(NoteId.of(noteId))

        verifyAll {
            updatedNote.title.value() == 'Updated Test Note'
            updatedNote.content.value() == 'Updated Lorem ipsum'
            updatedNote.users.get(0).emailValue() == 'john.doe@supernote.com'
            updatedNote.users.get(0).roleValue().toString() == 'OWNER'
            updatedNote.version.value() == 0
            updatedNote.metadata.expireAtValue().value().get() == Instant.parse("2024-09-10T21:00:00Z")
        }
    }

    def 'should delete a note'() {
        given:
        def noteId = noteService.createNote(createNoteRequest).block().id()

        when:
        noteService.deleteNote(noteId).block()

        then:
        repository.getById(NoteId.of(noteId)) == null
    }

    def 'should delete all notes'() {
        given:
        noteService.createNote(createNoteRequest).block().id()
        noteService.createNote(authentication, createNoteRequest).block().id()
        noteService.createNote(authentication, createNoteRequest).block().id()
        noteService.createNote(authentication, createNoteRequest).block().id()

        when:
        noteService.deleteNotes().block()

        then:
        repository.size() == 0
    }

    def cleanup() {
        repository.clear()
    }

}
