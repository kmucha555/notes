package com.mkjb.testhelper;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.Objects;


public class MongoDBContainerStub extends MongoDBContainer {
    private static final String MONGODB_DATABASE_NAME_DEFAULT = "test";
    private static final String MONGODB_LOCAL_URL = "localhost";
    private static final int MONGODB_DEFAULT_PORT = 27017;
    private MountableFile importFilePath;
    private String containerPath;

    public MongoDBContainerStub() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public boolean isCreated() {
        return true;
    }

    @Override
    public void copyFileToContainer(final MountableFile mountableFile, final String containerPath) {
        this.importFilePath = mountableFile;
        this.containerPath = containerPath;
    }

    @Override
    public ExecResult execInContainer(final String... command) throws IOException, InterruptedException {
        final String importFileCommand = String.join(" ", command).replace(containerPath, importFilePath.getFilesystemPath());
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(importFileCommand);
        pr.waitFor();
        //How to return ExecResults from here?
        return null;
    }

    @Override
    public String getReplicaSetUrl() {
        return String.format(
                "mongodb://%s:%d/%s",
                MONGODB_LOCAL_URL,
                MONGODB_DEFAULT_PORT,
                MONGODB_DATABASE_NAME_DEFAULT
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final MongoDBContainerStub that = (MongoDBContainerStub) o;
        return Objects.equals(importFilePath, that.importFilePath) && Objects.equals(containerPath, that.containerPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), importFilePath, containerPath);
    }
}
