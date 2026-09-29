package duncanscott.org.groovy.utils.json.util

import spock.lang.Specification
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ObjectNode

class JsonUtilSpec extends Specification {

    private static final JsonMapper MAPPER = JsonMapper.builder().build()

    void "maps, arrays, scalars, nulls, and dates round trip through JSON"() {
        given:
        def value = [
            count: 3,
            enabled: true,
            nested: [label: 'sample', absent: null],
            items: [1, 'two', false, null],
            created: DateUtil.stringToDate('1993-07-28T21:39:07.543Z')
        ]

        when:
        def tree = JsonUtil.toJson(value)
        def restored = MAPPER.readTree(JsonUtil.toString(tree))

        then:
        restored == MAPPER.readTree('''{
            "count": 3, "enabled": true,
            "nested": {"label": "sample", "absent": null},
            "items": [1, "two", false, null],
            "created": "1993-07-28T21:39:07.543Z"
        }''')
        JsonUtil.toJson(tree).is(tree)
        JsonUtil.toJson(null).isNull()
    }

    void "null removal traverses nested objects without modifying its input"() {
        given:
        String original = '{"missing":null,"child":{"missing":null,"kept":1},"items":[null,2]}'
        ObjectNode input = (ObjectNode) MAPPER.readTree(original)

        when:
        ObjectNode clean = JsonUtil.removeNulls(input)

        then:
        clean == MAPPER.readTree('{"child":{"kept":1},"items":[null,2]}')
        input == MAPPER.readTree(original)
    }
}
