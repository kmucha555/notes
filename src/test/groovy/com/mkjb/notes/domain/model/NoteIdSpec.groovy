package com.mkjb.notes.domain.model

import com.mkjb.notes.shared.exception.NoteValidationException
import spock.lang.Specification
import spock.lang.Unroll

class NoteIdSpec extends Specification {

    def 'should properly create note id'() {
        when:
        def noteId = NoteId.of('61e2e6ee6e5c100430145b0f')

        then:
        noteId.value() == '61e2e6ee6e5c100430145b0f'
    }

    @Unroll
    def 'should throw exception when note id is #id'() {
        when:
        NoteId.of(id)

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'Note id must not be empty'

        where:
        id << [null, '', ' ']
    }

    def 'should throw exception when note id is not parsable to ObjectId'() {
        when:
        NoteId.of('abc')

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'Note id must be in proper format'
    }

}
