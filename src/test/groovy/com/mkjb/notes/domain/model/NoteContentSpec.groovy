package com.mkjb.notes.domain.model

import com.mkjb.notes.shared.exception.NoteValidationException
import spock.lang.Specification
import spock.lang.Unroll

class NoteContentSpec extends Specification {

    def 'should properly create note content'() {
        when:
        def noteContent = NoteContent.of('Test Content')

        then:
        noteContent.value() == 'Test Content'
    }

    def 'should properly create note content when content does not exceed max length'() {
        given:
        def maxLengthContent = (1..1500).join('').substring(0, 2048)

        when:
        def noteContent = NoteContent.of(maxLengthContent)

        then:
        noteContent.value() == maxLengthContent
    }

    @Unroll
    def 'should throw exception when content is #content'() {
        when:
        NoteContent.of(content)

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'Note content must not be empty'

        where:
        content << [null, '', ' ']
    }

    def 'should throw exception when content is to long'() {
        given:
        def maxLengthContent = (1..1500).join('').substring(0, 2049)

        when:
        NoteContent.of(maxLengthContent)

        then:
        def exception = thrown(NoteValidationException)
        and:
        exception.message == 'Note content too long'
    }

}
