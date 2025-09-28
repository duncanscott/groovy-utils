package duncanscott.org.groovy.utils.json.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.lang.GroovyObject
import groovy.lang.MetaClass
import groovy.util.logging.Slf4j

@Slf4j
class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    static boolean isNull(Object obj) {
        return obj == null
    }

    static String camelCaseToDash(String s) {
        return s.replaceAll(/\B[A-Z]/) { '-' + it }.toLowerCase()
    }

    static Set<String> extractPropertiesToExclude(Object obj = null) {
        Set<String> propertiesToExclude
        if (obj == null) {
            propertiesToExclude = Object.metaClass.properties*.name as Set
        } else if (obj instanceof MetaClass) {
            propertiesToExclude = ((MetaClass) obj).properties*.name as Set
        } else if (obj instanceof Iterable) {
            propertiesToExclude = new HashSet<String>()
            ((Iterable)obj).each { propertyToExclude ->
                propertiesToExclude.addAll(extractPropertiesToExclude(propertyToExclude))
            }
        } else if (obj instanceof CharSequence) {
            propertiesToExclude = new HashSet<String>()
            propertiesToExclude.add(obj.toString())
        } else {
            propertiesToExclude = extractPropertiesToExclude(obj.metaClass)
        }
        return propertiesToExclude
    }


    static Map<String, Object> groovyObjectToJson(GroovyObject obj, Object propertiesToScreenOut = null) {
        log.debug "converting groovy object ${obj} to JSON"
        Map<String, Object> jsonObj = new LinkedHashMap<>()
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
                    if (jsonVal != null && !((jsonVal instanceof List && ((List)jsonVal).isEmpty()) || (jsonVal instanceof Map && ((Map)jsonVal).isEmpty()))) {
                        log.debug "returned value is of class ${jsonVal.class?.name}"
                        jsonObj[jsonName] = jsonVal
                    }
                }
            }
        }
        jsonObj
    }

    static String toString(Object o) {
        try {
            return MAPPER.writeValueAsString(o)
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize object to JSON string, falling back to toString()", e)
            return o.toString()
        }
    }

    static void mergeMapsNoReplace(Map map, Map mapToMerge, boolean throwErrorOnConflict = false) {
        mapToMerge?.each { key, val ->
            if (map.containsKey(key)) {
                Object existingVal = map[key]
                if (isNull(existingVal)) {
                    map[key] = val
                } else if (existingVal instanceof Map && val instanceof Map) {
                    mergeMapsNoReplace((Map)existingVal, (Map)val, throwErrorOnConflict)
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
            return toJsonObject((Map)obj)
        }
        if (obj instanceof Iterable) {
            return toJsonArray((Iterable)obj)
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
            return groovyObjectToJson((GroovyObject)obj)
        }
        return "$obj".toString()
    }


    static Map<String, Object> toJsonObject(Map obj) {
        Map<String, Object> json = new LinkedHashMap<>()
        obj?.each { key, val ->
            json["${key}"] = toJson(val)
        }
        return json
    }


    static List<Object> toJsonArray(Iterable obj) {
        List<Object> json = new ArrayList<>()
        obj?.each { val ->
            json << toJson(val)
        }
        return json
    }


    static Map<String, Object> removeNulls(Map<String, Object> json) {
        Map<String, Object> clean = new LinkedHashMap<>()
        json.each { key, val ->
            if (val instanceof Map) {
                clean[key] = removeNulls((Map)val)
            } else if (!isNull(val)) {
                clean[key] = val
            }
        }
        clean
    }

}
