package com.mkjb.notes.domain.model

import com.mkjb.notes.shared.exception.NoteValidationException
import spock.lang.Specification
import spock.lang.Unroll

class NoteTitleSpec extends Specification {

    def 'should properly create note title'() {
        when:
        def noteTitle = NoteTitle.of('Test Note')

        then:
        noteTitle.value() == 'Test Note'
    }

    def 'should properly create note title when title does not exceed max length'() {
        given:
        def maxLengthTitle = (1..150).join('').substring(0, 256)

        when:
        def noteTitle = NoteTitle.of(maxLengthTitle)

        then:
        noteTitle.value() == maxLengthTitle
    }

    @Unroll
    def 'should throw exception when title is #title'() {
        when:
        NoteTitle.of(title)

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'The note title must not be empty'

        where:
        title << [null, '', ' ']
    }

    def 'should throw exception when title is to long'() {
        given:
        def maxLengthTitle = (1..150).join('').substring(0, 257)

        when:
        NoteTitle.of(maxLengthTitle)

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'The note title too long'
    }

}
