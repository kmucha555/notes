package com.mkjb.notes

import com.mkjb.testhelper.MongoContainer
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.MongoDBContainer
import spock.lang.Shared
import spock.lang.Specification

@MongoContainer(testContainers = 'false')
class BaseSpec extends Specification implements TestPropertyProvider {

    @Shared
    MongoDBContainer db

    @Override
    Map<String, String> getProperties() {
        return Map.of('mongodb.uri', db.getReplicaSetUrl())
    }
}
