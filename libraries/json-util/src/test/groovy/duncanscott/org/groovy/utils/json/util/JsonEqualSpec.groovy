package duncanscott.org.groovy.utils.json.util

import spock.lang.Specification
import tools.jackson.databind.json.JsonMapper

class JsonEqualSpec extends Specification {

    private static final JsonMapper MAPPER = JsonMapper.builder().build()

    void "JSON equality compares nested values and treats missing properties as null"() {
        expect:
        JsonEqual.areEqual(MAPPER.readTree(left), MAPPER.readTree(right)) == equal

        where:
        left                              | right                             | equal
        '{"a":1,"b":{"c":[2,3]}}'       | '{"b":{"c":[2,3]},"a":1}'       | true
        '{"a":{"b":1}}'                 | '{"a":{"b":2}}'                 | false
        '{"a":[1,2]}'                    | '{"a":[2,1]}'                    | false
        '{"a":[1]}'                      | '{"a":[1,2]}'                    | false
        '{"a":null}'                     | '{}'                              | true
        '{}'                              | '{"extra":1}'                     | false
        'null'                            | 'null'                            | true
        'null'                            | '1'                               | false
    }
}
