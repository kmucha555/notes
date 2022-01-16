package com.mkjb.notes.adapters.api.command

import com.mkjb.notes.domain.model.NoteId
import com.mkjb.notes.domain.ports.InMemoryNoteCommandRepository
import com.mkjb.notes.domain.ports.NoteFacade
import com.mkjb.notes.shared.exception.NoteException
import io.micronaut.http.HttpStatus
import io.micronaut.security.authentication.Authentication
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

class NoteServiceSpec extends Specification implements NoteCommandTestData {

    def repository = new InMemoryNoteCommandRepository()
    def noteFacade = new NoteFacade(repository)
    def noteService = new NoteService(noteFacade)
    def owner = Authentication.build('owner john', ['email': 'john.doe@supernote.com'])
    def editor = Authentication.build('editor mark', ['email': 'editor.doe@supernote.com'])
    def viewer = Authentication.build('viewer bob', ['email': 'viewer.doe@supernote.com'])

    def 'should create a note'() {
        when:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()

        then:
        def createdNote = repository.findByNoteId(NoteId.of(noteId)).block()

        verifyAll {
            createdNote.title().value() == 'Test Note'
            createdNote.content().value() == 'Lorem ipsum'
            createdNote.users().get(0).email().value() == 'john.doe@supernote.com'
            createdNote.users().get(0).role().name() == 'OWNER'
            createdNote.version().value() == 0
            createdNote.metadata().expireAtValue().value().get() == Instant.parse("2023-06-01T21:00:00Z")
        }
    }

    def 'OWNER should update a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.updateNote(owner, noteId, updateNoteRequest).block()

        then:
        def updatedNote = repository.findByNoteId(NoteId.of(noteId)).block()

        verifyAll {
            updatedNote.title().value() == 'Updated Test Note'
            updatedNote.content().value() == 'Updated Lorem ipsum'
            updatedNote.metadata().expireAtValue().value().get() == Instant.parse("2024-09-10T21:00:00Z")
        }
    }

    def 'EDITOR should update a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.updateNote(editor, noteId, updateNoteRequest).block()

        then:
        def updatedNote = repository.findByNoteId(NoteId.of(noteId)).block()

        verifyAll {
            updatedNote.title().value() == 'Updated Test Note'
            updatedNote.content().value() == 'Updated Lorem ipsum'
            updatedNote.metadata().expireAtValue().value().get() == Instant.parse("2024-09-10T21:00:00Z")
        }
    }

    def 'EDITOR should update a note when is also acting as VIEWER'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()
        noteService.invite(owner, noteId, new InviteRequest('editor.doe@supernote.com', 'VIEWER')).block()

        when:
        noteService.updateNote(editor, noteId, updateNoteRequest).block()

        then:
        def updatedNote = repository.findByNoteId(NoteId.of(noteId)).block()

        verifyAll {
            updatedNote.title().value() == 'Updated Test Note'
            updatedNote.content().value() == 'Updated Lorem ipsum'
            updatedNote.metadata().expireAtValue().value().get() == Instant.parse("2024-09-10T21:00:00Z")
        }
    }

    def 'should throw exception when VIEWER tries to update a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.updateNote(viewer, noteId, updateNoteRequest).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'
    }

    def 'OWNER should delete a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()

        when:
        noteService.deleteNote(owner, noteId).block()

        then:
        repository.size() == 0
    }

    def 'should throw exception when VIEWER tries to delete a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.deleteNote(viewer, noteId).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'
    }

    def 'should throw exception when EDITOR tries to delete a note'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.deleteNote(editor, noteId).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'
    }

    def 'OWNER should delete all notes'() {
        given:
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()

        when:
        noteService.deleteNotes(owner).block()

        then:
        repository.size() == 0
    }

    def 'should throw exception when VIEWER tries to delete all notes'() {
        given:
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()

        when:
        noteService.deleteNotes(viewer).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'
    }

    def 'should throw exception when EDITOR tries to delete all notes'() {
        given:
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()
        noteService.createNote(owner, createNoteRequest).block()

        when:
        noteService.deleteNotes(editor).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'
    }

    @Unroll
    def 'should share a note with role #invitedUser'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()

        when:
        noteService.invite(owner, noteId, invitedUser).block()

        then:
        def createdNote = repository.findByNoteId(NoteId.of(noteId)).block()
        createdNote.users().any { it.email().value() == invitedUser.email() && it.role().name() == invitedUser.role() }

        where:
        invitedUser << [inviteViewer, inviteEditor, inviteOwner]
    }

    def 'should invite already invited user in different role'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        def viewerWithNewRole = new InviteRequest('viewer.doe@supernote.com', 'EDITOR')

        when:
        noteService.invite(owner, noteId, viewerWithNewRole).block()

        then:
        def createdNote = repository.findByNoteId(NoteId.of(noteId)).block()
        createdNote.users().any { it.email().value() == viewerWithNewRole.email() && it.role().name() == viewerWithNewRole.role() }
    }

    @Unroll
    def 'should throw exception when VIEWER is trying to share note to #invitedUser'() {
        given:
        def noteId = noteService.createNote(owner, createNoteRequest).block().id()
        noteService.invite(owner, noteId, inviteViewer).block()
        noteService.invite(owner, noteId, inviteEditor).block()

        when:
        noteService.invite(viewer, noteId, invitedUser).block()

        then:
        def exception = thrown(NoteException)
        and:
        exception.status == HttpStatus.FORBIDDEN
        and:
        exception.message == 'You are not authorized to perform the operation'

        where:
        invitedUser << [inviteEditor, inviteOwner]
    }


    def cleanup() {
        repository.clear()
    }

}
