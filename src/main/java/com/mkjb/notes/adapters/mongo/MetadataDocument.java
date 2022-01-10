package com.mkjb.notes.adapters.mongo;

import com.mkjb.notes.domain.model.CreatedAt;
import com.mkjb.notes.domain.model.ExpireAt;
import com.mkjb.notes.domain.model.NoteMetadata;
import com.mkjb.notes.domain.model.ModifiedAt;

import java.time.Instant;

public class MetadataDocument {

    private Instant createdAt;
    private Instant modifiedAt;
    private Instant expireAt;

    public MetadataDocument() {
    }

    public MetadataDocument(final Instant createdAt, final Instant modifiedAt, final Instant expireAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.expireAt = expireAt;
    }

    public NoteMetadata toDomain() {
        return NoteMetadata.of(ExpireAt.of(expireAt), CreatedAt.of(createdAt), ModifiedAt.of(modifiedAt));
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(final Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Instant getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(final Instant expiredAt) {
        this.expireAt = expiredAt;
    }

    static MetadataDocumentBuilder metadataDocument() {
        return new MetadataDocumentBuilder();
    }

    static final class MetadataDocumentBuilder {

        private Instant createdAt;
        private Instant modifiedAt;
        private Instant expireAt;

        MetadataDocumentBuilder withCreatedAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        MetadataDocumentBuilder withModifiedAt(final Instant modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        MetadataDocumentBuilder withExpiredAt(final Instant expireAt) {
            this.expireAt = expireAt;
            return this;
        }

        MetadataDocument build() {
            return new MetadataDocument(createdAt, modifiedAt, expireAt);
        }

    }
}
