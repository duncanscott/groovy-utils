package duncanscott.org.groovy.utils.json.util

import groovy.util.logging.Slf4j
import org.json.simple.JSONArray
import org.json.simple.JSONAware
import org.json.simple.JSONObject

/**
 * Created by dscott on 1/23/2015.
 */
@Slf4j
class JsonUtil {

    static boolean isNull(Object obj) {
        return obj == null
    }

    static String camelCaseToDash(String s) {
        return s.replaceAll(/\B[A-Z]/) { '-' + it }.toLowerCase()
    }

    static Set<String> extractPropertiesToExclude(Object obj = null) {
        Set<String> propertiesToExclude = null
        if (obj == null) {
            propertiesToExclude = Object.metaClass.properties*.name.toSet()
        } else if (obj instanceof MetaClass) {
            propertiesToExclude = obj.properties*.name.toSet()
        } else if (obj instanceof Iterable) {
            propertiesToExclude = []
            obj.each { propertyToExclude ->
                propertiesToExclude += extractPropertiesToExclude(propertyToExclude)
            }
        } else if (obj instanceof CharSequence) {
            propertiesToExclude = []
            propertiesToExclude << obj.toString()
        } else {
            propertiesToExclude = extractPropertiesToExclude(obj.metaClass)
        }
        return propertiesToExclude
    }


    static JSONObject groovyObjectToJson(GroovyObject obj, Object propertiesToScreenOut = null) {
        log.debug "converting groovy object ${obj} to JSON"
        JSONObject jsonObj = new JSONObject()
        if (obj != null) {
            Set<String> propertiesToExclude = extractPropertiesToExclude(propertiesToScreenOut)
            obj.metaClass.properties*.name.each { String name ->
                if (!propertiesToExclude.contains(name)) {
                    String jsonName = camelCaseToDash(name)
                    Object propVal = obj.getProperty(name)
                    if (obj.is(propVal)) {
                        return
                    }
                    log.debug "converting property ${name} to JSON"
                    Object jsonVal = toJson(propVal)
                    if (jsonVal != null && !((jsonVal instanceof JSONArray && !jsonVal) || (jsonVal instanceof JSONObject && !jsonVal))) {
                        log.debug "returned value is of class ${jsonVal.class?.name}"
                        jsonObj[jsonName] = jsonVal
                    }
                }
            }
        }
        jsonObj
    }

    static String toString(Object o) {
        if (o instanceof JSONAware) {
            return o.toJSONString()
        } else {
            return "${o}"
        }
    }

    static void mergeMapsNoReplace(Map map, Map mapToMerge, boolean throwErrorOnConflict = false) {
        mapToMerge?.each { key, val ->
            if (map.containsKey(key)) {
                Object existingVal = map[key]
                if (isNull(existingVal)) {
                    map[key] = val
                }  else if (existingVal instanceof Map && val instanceof Map) {
                    mergeMapsNoReplace(existingVal,val)
                } else if (throwErrorOnConflict) {
                    throw new RuntimeException("maps share key ${key}")
                }
            } else {
                map[key] = val
            }
        }
    }

    static Object toJson(Object obj) {
        if (isNull(obj)) {
            return null
        }
        if (obj instanceof Date) {
            return DateUtil.dateToString((Date) obj)
        }
        if (obj instanceof Map) {
            return toJsonObject(obj)
        }
        if (obj instanceof Iterable) {
            return toJsonArray(obj)
        }
        if (obj instanceof Number) {
            return obj
        }
        if (obj instanceof CharSequence) {
            return obj.toString()
        }
        if (obj instanceof Boolean) {
            return obj.booleanValue()
        }
        if (obj instanceof GroovyObject) {
            return groovyObjectToJson(obj)
        }
        return obj
    }


    static JSONObject toJsonObject(Map obj) {
        if (obj instanceof JSONObject) {
            return obj
        }
        JSONObject json = new JSONObject()
        obj?.each { key, val ->
            json["${key}"] = toJson(val)
        }
        return json
    }


    static JSONArray toJsonArray(Iterable obj) {
        if (obj instanceof JSONArray) {
            return obj
        }
        JSONArray json = new JSONArray()
        obj?.each { val ->
            json << toJson(val)
        }
        return json
    }


    static JSONObject removeNulls(JSONObject json) {
        JSONObject clean = new JSONObject()
        json.each { key,val ->
            if (val instanceof JSONObject) {
                clean[key] = removeNulls(val)
            } else if(!isNull(val)) {
                clean[key] = val
            }
        }
        clean
    }

}
