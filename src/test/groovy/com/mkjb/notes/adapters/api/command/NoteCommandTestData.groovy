package com.mkjb.notes.adapters.api.command

import java.time.Instant

trait NoteCommandTestData {
    def createNoteRequest = new NoteRequest(
            'Test Note',
            'Lorem ipsum',
            Instant.parse("2023-06-01T21:00:00Z"),
            0)

    def updateNoteRequest = new NoteRequest(
            'Updated Test Note',
            'Updated Lorem ipsum',
            Instant.parse("2024-09-10T21:00:00Z"),
            0)

    def static inviteViewer = new InviteRequest('viewer.doe@supernote.com', 'VIEWER')
    def static inviteEditor = new InviteRequest('editor.doe@supernote.com', 'EDITOR')
    def static inviteOwner = new InviteRequest('owner.doe@supernote.com', 'OWNER')

}