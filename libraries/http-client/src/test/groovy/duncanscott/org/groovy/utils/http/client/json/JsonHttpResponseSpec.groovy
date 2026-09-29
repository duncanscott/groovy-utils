package duncanscott.org.groovy.utils.http.client.json

import duncanscott.org.groovy.utils.http.client.base.TextResponse
import spock.lang.Specification
import tools.jackson.core.JacksonException
import tools.jackson.databind.JsonNode

class JsonHttpResponseSpec extends Specification {

    void "JSON response exposes and caches a Jackson 3 tree"() {
        given:
        def response = new JsonHttpResponse(textResponse: new TextResponse(
            text: '{"count":2,"items":["one","two"],"optional":null}'
        ))

        when:
        JsonNode json = response.json

        then:
        json.get('count').asInt() == 2
        json.get('items').get(1).asText() == 'two'
        json.get('optional').isNull()
        response.json.is(json)
    }

    void "missing response body returns null"() {
        expect:
        new JsonHttpResponse().json == null
    }

    void "invalid JSON propagates a Jackson parsing error"() {
        given:
        def response = new JsonHttpResponse(textResponse: new TextResponse(text: '{"broken":}'))

        when:
        response.json

        then:
        thrown(JacksonException)
    }
}
