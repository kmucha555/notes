package com.mkjb.notes.domain.model;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class Note {
    private final NoteId id;
    private final NoteTitle title;
    private final NoteContent content;
    private final Set<NoteUser> users;
    private final Metadata metadata;
    private final Version version;

    private Note(final NoteId id, final NoteTitle title, final NoteContent content, final Set<NoteUser> users,
                 final Metadata metadata, final Version version) {
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

    public Set<NoteUser> getUsers() {
        return users;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Version getVersion() {
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
                .add("content=" + content)
                .add("users=" + users)
                .add("metadata=" + metadata)
                .add("version=" + version)
                .toString();
    }

    public static NoteBuilder note() {
        return new NoteBuilder();
    }

    public static final class NoteBuilder {
        private NoteId id;
        private NoteTitle title;
        private NoteContent content;
        private Set<NoteUser> users;
        private Metadata metadata;
        private Version version;

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

        public NoteBuilder withUsers(final Set<NoteUser> users) {
            this.users = users;
            return this;
        }

        public NoteBuilder withMetadata(final Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public NoteBuilder withVersion(final Version version) {
            this.version = version;
            return this;
        }

        public Note build() {
            return new Note(id, title, content, users, metadata, version);
        }

    }
}
