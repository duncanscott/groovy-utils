package duncanscott.org.groovy.utils.json.util

import groovy.util.logging.Slf4j
import org.codehaus.groovy.runtime.StackTraceUtils
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Created by duncanscott on 1/3/15.
 */
@Slf4j
class JsonEqual {

    static boolean areEqual(o1, o2) {
        try {
            if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                log.debug "comparing:\n${o1}\n${o2}"
            }
            checkElementsEqual(o1,o2)
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
                throw new NotEqualException(o1,o2)
            }
            return true
        }
        return false
    }

    private static void checkObjectsEqual(JSONObject o1, JSONObject o2) {
        (o1.keySet() + o2.keySet()).each { key ->
            log "checking map values equal for key ${key}: ${o1[key]} ${o2[key]}"
            checkElementsEqual(o1[key],o2[key])
        }
    }

    private static void checkArraysEqual(JSONArray a1, JSONArray a2) {
        log "checking arrays 1: ${a1}"
        log "checking arrays 2: ${a2}"
        if (a1.size() != a2.size()) {
            throw new NotEqualException(a1,a2)
        }
        log "checking array elements"
        for (int i = 0; i < a1.size(); i++) {
            checkElementsEqual(a1[i],a2[i])
        }
    }

    private static void checkElementsEqual(Object e1, Object e2) {
        log "checking elements: ${e1} (${e1?.class}); ${e2} (${e2?.class})"
        if(checkNullEqual(e1,e2)) {
            return
        }
        if (e1.is(e2)) {
            return
        }
        if (e1 instanceof JSONObject || e2 instanceof JSONObject) {
            if (e1 instanceof JSONObject && e2 instanceof JSONObject) {
                checkObjectsEqual(e1,e2)
                return
            }
            throw new NotEqualException(e1,e2)
        }
        if (e1 instanceof JSONArray || e2 instanceof JSONArray) {
            if (e1 instanceof JSONArray && e2 instanceof JSONArray) {
                checkArraysEqual(e1,e2)
                return
            }
            throw new NotEqualException(e1,e2)
        }
        if (e1 instanceof Number && e2 instanceof Number) {
            if (e1 != e2) {
                throw new NotEqualException(e1,e2)
            }
        } else if (!e1.toString().equals(e2.toString())) {
            throw new NotEqualException(e1,e2)
        }
    }



}