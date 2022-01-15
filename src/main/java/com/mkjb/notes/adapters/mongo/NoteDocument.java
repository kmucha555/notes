package com.mkjb.notes.adapters.mongo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public final class NoteDocument {

    private ObjectId id;
    private String title;
    private String content;
    private List<UserDocument> users;
    private MetadataDocument metadata;
    private int version;

    public NoteDocument() {
    }

    NoteDocument(final ObjectId id, final String title, final String content,
                 final List<UserDocument> users, final MetadataDocument metadata,
                 final int version) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.users = users;
        this.metadata = metadata;
        this.version = version;
    }

    Map<String, Object> toNoteUpdate() {
        return Map.of(
                "id", id,
                "title", title,
                "content", content,
                "users", users,
                "version", version
        );
    }

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonGetter("id")
    public String idAsString() {
        return id.toString();
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public List<UserDocument> getUsers() {
        return users;
    }

    public void setUsers(final List<UserDocument> users) {
        this.users = users;
    }

    public MetadataDocument getMetadata() {
        return metadata;
    }

    public void setMetadata(final MetadataDocument metadata) {
        this.metadata = metadata;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    static NoteDocumentBuilder noteDocument() {
        return new NoteDocumentBuilder();
    }

    static final class NoteDocumentBuilder {
        private ObjectId id;
        private String title;
        private String content;
        private List<UserDocument> users;
        private MetadataDocument metadata;
        private int version;

        private NoteDocumentBuilder() {
        }

        NoteDocumentBuilder withId(final ObjectId id) {
            this.id = id;
            return this;
        }

        NoteDocumentBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        NoteDocumentBuilder withContent(final String content) {
            this.content = content;
            return this;
        }

        NoteDocumentBuilder withUsers(final List<UserDocument> users) {
            this.users = users;
            return this;
        }

        NoteDocumentBuilder withMetadata(final MetadataDocument metadata) {
            this.metadata = metadata;
            return this;
        }

        NoteDocumentBuilder withVersion(final int version) {
            this.version = version;
            return this;
        }

        NoteDocument build() {
            return new NoteDocument(id, title, content, users, metadata, version);
        }

    }
}
