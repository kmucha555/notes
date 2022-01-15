package com.mkjb.notes.adapters.mongo;

import java.time.Instant;
import java.util.Map;

public final class MetadataDocument {

    private Map<String, Instant> metadata;

    private Instant createdAt;
    private Instant modifiedAt;
    private Instant expireAt;

    public MetadataDocument() {
    }

    MetadataDocument(final Instant createdAt, final Instant modifiedAt, final Instant expireAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.expireAt = expireAt;
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
