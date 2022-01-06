package com.mkjb.notes.adapters.mongo;

import java.time.Instant;

public class MetadataDocument {

    private Instant createdAt;
    private Instant modifiedAt;
    private Instant expiredAt;

    public MetadataDocument(final Instant createdAt, final Instant modifiedAt, final Instant expiredAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.expiredAt = expiredAt;
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

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(final Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    static MetadataDocumentBuilder metadataDocument() {
        return new MetadataDocumentBuilder();
    }

    static final class MetadataDocumentBuilder {

        private Instant createdAt;
        private Instant modifiedAt;
        private Instant expiredAt;

         MetadataDocumentBuilder withCreatedAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

         MetadataDocumentBuilder withModifiedAt(final Instant modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

         MetadataDocumentBuilder withExpiredAt(final Instant expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

         MetadataDocument build() {
            return new MetadataDocument(createdAt, modifiedAt, expiredAt);
        }

    }
}
