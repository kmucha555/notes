package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.*;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;

public class NoteDocument {

    @BsonId
    private String id;
    private String title;
    private String content;
    private List<UserDocument> users;
    private MetadataDocument metadata;
    private int version;

    public NoteDocument(final String id, final String title, final String content,
                        final List<UserDocument> users, final MetadataDocument metadata,
                        final int version) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.users = users;
        this.metadata = metadata;
        this.version = version;
    }

    public Note toDomain() {
        return Note
                .note()
                .withId(NoteId.of(id))
                .withTitle(NoteTitle.of(title))
                .withContent(NoteContent.of(content))
                .withUsers(NoteUser)
                .withMetadata(Metadata.of(metadata))
                .withVersion(Version.of(version))
                .build();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
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
        private String id;
        private String title;
        private String content;
        private List<UserDocument> users;
        private MetadataDocument metadata;
        private int version;

        private NoteDocumentBuilder() {
        }

        NoteDocumentBuilder withId(final String id) {
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
