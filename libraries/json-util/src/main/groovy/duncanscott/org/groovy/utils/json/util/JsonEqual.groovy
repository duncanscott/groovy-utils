package duncanscott.org.groovy.utils.json.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import groovy.util.logging.Slf4j
import org.codehaus.groovy.runtime.StackTraceUtils

@Slf4j
class JsonEqual {

    static boolean areEqual(o1, o2) {
        try {
            if (o1 instanceof ObjectNode && o2 instanceof ObjectNode) {
                log.debug "comparing:\n${o1}\n${o2}"
            }
            checkElementsEqual(o1, o2)
        } catch (NotEqualException ne) {
            log.debug "${ne}", StackTraceUtils.sanitizeRootCause(ne)
            return false
        }
        return true
    }

    private static void log(String msg) {
        log.debug msg
    }

    private static boolean checkNullEqual(Object o1, Object o2) {
        if (JsonUtil.isNull(o1) || JsonUtil.isNull(o2)) {
            if (!JsonUtil.isNull(o1) || !JsonUtil.isNull(o2)) {
                throw new NotEqualException(o1, o2)
            }
            return true
        }
        return false
    }

    private static void checkObjectsEqual(ObjectNode o1, ObjectNode o2) {
        Set<String> keys = o1.fieldNames().toSet() + o2.fieldNames().toSet()
        keys.each { String key ->
            log "checking map values equal for key ${key}: ${o1.get(key)} ${o2.get(key)}"
            checkElementsEqual(o1.get(key), o2.get(key))
        }
    }

    private static void checkArraysEqual(ArrayNode a1, ArrayNode a2) {
        log "checking arrays 1: ${a1}"
        log "checking arrays 2: ${a2}"
        if (a1.size() != a2.size()) {
            throw new NotEqualException(a1, a2)
        }
        log "checking array elements"
        for (int i = 0; i < a1.size(); i++) {
            checkElementsEqual(a1.get(i), a2.get(i))
        }
    }

    private static void checkElementsEqual(Object e1, Object e2) {
        log "checking elements: ${e1} (${e1?.class}); ${e2} (${e2?.class})"
        if (checkNullEqual(e1, e2)) {
            return
        }
        if (e1.is(e2)) {
            return
        }

        JsonNode n1 = (e1 instanceof JsonNode) ? (JsonNode) e1 : JsonUtil.toJson(e1)
        JsonNode n2 = (e2 instanceof JsonNode) ? (JsonNode) e2 : JsonUtil.toJson(e2)

        if (n1.isObject() && n2.isObject()) {
            checkObjectsEqual((ObjectNode) n1, (ObjectNode) n2)
            return
        }
        if (n1.isArray() && n2.isArray()) {
            checkArraysEqual((ArrayNode) n1, (ArrayNode) n2)
            return
        }

        if (!n1.equals(n2)) {
            throw new NotEqualException(e1, e2)
        }
    }
}
