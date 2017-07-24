package com.github.mkopylec.errorest.specification

import com.github.mkopylec.errorest.BasicSpec
import com.github.mkopylec.errorest.exceptions.ErrorestResponseException
import com.github.mkopylec.errorest.response.Error
import spock.lang.Unroll

import static com.github.mkopylec.errorest.configuration.ErrorestProperties.ResponseBodyFormat.FULL
import static com.github.mkopylec.errorest.configuration.ErrorestProperties.ResponseBodyFormat.WITHOUT_DESCRIPTIONS
import static org.springframework.http.HttpHeaders.ACCEPT
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE

class MessageNotReadableErrorsSpec extends BasicSpec {

    @Unroll
    def "Should get full error from #uri request for 'Accept: #acceptHeader' header"() {
        given:
        responseBodyFormat = FULL

        when:
        sendRequest POST, uri, [(ACCEPT): acceptHeader], [request: 'body']

        then:
        def ex = thrown ErrorestResponseException
        ex.statusCode == BAD_REQUEST
        ex.responseBodyAsErrors.errors[0].hasCode('HTTP_CLIENT_ERROR')
        ex.responseBodyAsErrors.errors[0].description.startsWith('Bad Request, ')

        where:
        uri                                | acceptHeader
        '/controller/message-not-readable' | APPLICATION_JSON_VALUE
        '/controller/message-not-readable' | APPLICATION_XML_VALUE
        '/filter/message-not-readable'     | APPLICATION_JSON_VALUE
        '/filter/message-not-readable'     | APPLICATION_XML_VALUE
    }

    @Unroll
    def "Should get error without description from #uri request for 'Accept: #acceptHeader' header"() {
        given:
        responseBodyFormat = WITHOUT_DESCRIPTIONS

        when:
        sendRequest POST, uri, [(ACCEPT): acceptHeader], [request: 'body']

        then:
        def ex = thrown ErrorestResponseException
        ex.statusCode == BAD_REQUEST
        ex.responseBodyAsErrors.errors == [new Error('HTTP_CLIENT_ERROR', 'N/A')]

        where:
        uri                                | acceptHeader
        '/controller/message-not-readable' | APPLICATION_JSON_VALUE
        '/controller/message-not-readable' | APPLICATION_XML_VALUE
        '/filter/message-not-readable'     | APPLICATION_JSON_VALUE
        '/filter/message-not-readable'     | APPLICATION_XML_VALUE
    }
}