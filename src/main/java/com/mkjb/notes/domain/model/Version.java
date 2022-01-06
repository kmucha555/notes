package com.mkjb.notes.domain.model;

public final class Version {

    private final int version;

    public Version(int version) {
        this.version = version;
    }

    public static Version zero() {
        return new Version(0);
    }

    public static Version of(final int version) {
        return new Version(version);
    }

    public int value() {
        return version;
    }

}
