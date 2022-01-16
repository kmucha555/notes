package com.mkjb.notes.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class Note {
    private final NoteId id;
    private final NoteTitle title;
    private final NoteContent content;
    private final List<NoteUser> users;
    private final NoteMetadata metadata;
    private final NoteVersion version;

    private Note(final NoteId id, final NoteTitle title, final NoteContent content,
                 final List<NoteUser> users, final NoteMetadata metadata, final NoteVersion version) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.users = users;
        this.metadata = metadata;
        this.version = version;
    }

    public boolean isAbleToUpdateNote(final NoteUserEmail noteUserEmail) {
        return users
                .stream()
                .filter(noteUser -> noteUser.email().equals(noteUserEmail))
                .anyMatch(this::rolesThatCanUpdateNote);
    }

    private boolean rolesThatCanUpdateNote(final NoteUser noteUser) {
        return noteUser.role() == NoteUserRole.OWNER || noteUser.role() == NoteUserRole.EDITOR;
    }

    public boolean isAbleToShareNote(final NoteUserEmail noteUserEmail) {
        return isAbleToDeleteNote(noteUserEmail);
    }

    public boolean isAbleToDeleteNote(final NoteUserEmail noteUserEmail) {
        return users
                .stream()
                .filter(noteUser -> noteUser.email().equals(noteUserEmail))
                .anyMatch(this::rolesThatCanDeleteNote);
    }

    private boolean rolesThatCanDeleteNote(final NoteUser noteUser) {
        return noteUser.role() == NoteUserRole.OWNER;
    }

    public NoteId id() {
        return id;
    }

    public NoteTitle title() {
        return title;
    }

    public NoteContent content() {
        return content;
    }

    public List<NoteUser> users() {
        return users;
    }

    public NoteMetadata metadata() {
        return metadata;
    }

    public NoteVersion version() {
        return version;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Note note = (Note) o;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Note.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title=" + title)
                .toString();
    }

    public static NoteBuilder note() {
        return new NoteBuilder();
    }

    public static final class NoteBuilder {
        private NoteId id;
        private NoteTitle title;
        private NoteContent content;
        private List<NoteUser> users;
        private NoteMetadata metadata;
        private NoteVersion version;

        public NoteBuilder withId(final NoteId id) {
            this.id = id;
            return this;
        }

        public NoteBuilder withTitle(final NoteTitle title) {
            this.title = title;
            return this;
        }

        public NoteBuilder withContent(final NoteContent content) {
            this.content = content;
            return this;
        }

        public NoteBuilder withUsers(final List<NoteUser> users) {
            this.users = users;
            return this;
        }

        public NoteBuilder withMetadata(final NoteMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public NoteBuilder withVersion(final NoteVersion version) {
            this.version = version;
            return this;
        }

        public Note build() {
            return new Note(id, title, content, users, metadata, version);
        }

    }
}
