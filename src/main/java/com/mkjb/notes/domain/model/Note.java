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

    public NoteId getId() {
        return id;
    }

    public NoteTitle getTitle() {
        return title;
    }

    public NoteContent getContent() {
        return content;
    }

    public List<NoteUser> getUsers() {
        return users;
    }

    public NoteMetadata getMetadata() {
        return metadata;
    }

    public NoteVersion getVersion() {
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

        public NoteBuilder() {
        }

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
