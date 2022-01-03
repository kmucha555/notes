package com.mkjb.notes.domain.model;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Introspected
public class Note {
    private final NoteId noteId;
    private final NoteTitle title;
    private final NoteContent content;
    private final Set<User> users;
    private final Metadata metadata;

    Note(final NoteId noteId, final NoteTitle title, final NoteContent content, final Set<User> users, final Metadata metadata) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.users = users;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Note note = (Note) o;
        return Objects.equals(noteId, note.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Note.class.getSimpleName() + "[", "]")
                .add("noteId=" + noteId)
                .add("title=" + title)
                .add("content=" + content)
                .add("users=" + users)
                .add("metadata=" + metadata)
                .toString();
    }

}
