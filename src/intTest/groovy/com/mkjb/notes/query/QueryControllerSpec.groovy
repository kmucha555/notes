package com.mkjb.notes.query

import com.mkjb.notes.BaseSpec
import com.mkjb.notes.TestHttpClient
import com.mkjb.testhelper.Populate
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.testcontainers.spock.Testcontainers

@Testcontainers
@MicronautTest
@Populate
class QueryControllerSpec extends BaseSpec {

    @Inject
    TestHttpClient client

    def test() {
        expect:
        1 == 1
    }
}
