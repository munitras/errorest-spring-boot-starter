package com.github.mkopylec.errorest.specification

import com.github.mkopylec.errorest.BasicSpec
import com.github.mkopylec.errorest.exceptions.ExternalHttpRequestException
import spock.lang.Unroll

import static com.github.mkopylec.errorest.assertions.Assertions.assertThat
import static com.github.mkopylec.errorest.configuration.ErrorestProperties.ResponseBodyFormat.FULL
import static com.github.mkopylec.errorest.configuration.ErrorestProperties.ResponseBodyFormat.WITHOUT_DESCRIPTIONS
import static org.springframework.http.HttpHeaders.ACCEPT
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE

class ExceptionErrorsSpec extends BasicSpec {

    @Unroll
    def "Should get full error from #uri request for 'Accept: #acceptHeader' header"() {
        given:
        responseBodyFormat = FULL

        when:
        sendRequest POST, uri, [(ACCEPT): acceptHeader]

        then:
        def ex = thrown ExternalHttpRequestException
        assertThat(ex)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasErrorsId()
                .hasSingleError('UNEXPECTED_ERROR', description)

        where:
        uri                     | acceptHeader           | description
        '/controller/exception' | APPLICATION_JSON_VALUE | 'Exception from controller'
        '/controller/exception' | APPLICATION_XML_VALUE  | 'Exception from controller'
        '/filter/exception'     | APPLICATION_JSON_VALUE | 'Exception from servlet filer'
        '/filter/exception'     | APPLICATION_XML_VALUE  | 'Exception from servlet filer'
    }

    @Unroll
    def "Should get error without description from #uri request for 'Accept: #acceptHeader' header"() {
        given:
        responseBodyFormat = WITHOUT_DESCRIPTIONS

        when:
        sendRequest POST, uri, [(ACCEPT): acceptHeader]

        then:
        def ex = thrown ExternalHttpRequestException
        assertThat(ex)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasErrorsId()
                .hasSingleErrorWithoutDescription('UNEXPECTED_ERROR')

        where:
        uri                     | acceptHeader
        '/controller/exception' | APPLICATION_JSON_VALUE
        '/controller/exception' | APPLICATION_XML_VALUE
        '/filter/exception'     | APPLICATION_JSON_VALUE
        '/filter/exception'     | APPLICATION_XML_VALUE
    }
}