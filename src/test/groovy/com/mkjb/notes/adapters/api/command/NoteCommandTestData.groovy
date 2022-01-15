package com.mkjb.notes.adapters.api.command

import java.time.Instant

trait NoteCommandTestData {
    def createNoteRequest = new NoteRequest(
            'Test Note',
            'Lorem ipsum',
            Set.of(new NoteRequest.User('john.doe@supernote.com', 'OWNER')),
            Instant.parse("2023-06-01T21:00:00Z"),
            0)

    def updateNoteRequest = new NoteRequest(
            'Updated Test Note',
            'Updated Lorem ipsum',
            Set.of(new NoteRequest.User('john.doe@supernote.com', 'OWNER')),
            Instant.parse("2024-09-10T21:00:00Z"),
            0)

}