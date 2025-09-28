package duncanscott.org.groovy.utils.json.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import groovy.lang.GroovyObject
import groovy.lang.MetaClass
import groovy.util.logging.Slf4j

@Slf4j
class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    static boolean isNull(Object obj) {
        if (obj instanceof JsonNode) {
            return ((JsonNode) obj).isNull()
        }
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

    static ObjectNode groovyObjectToJson(GroovyObject obj, Object propertiesToScreenOut = null) {
        log.debug "converting groovy object ${obj} to JSON"
        ObjectNode jsonObj = MAPPER.createObjectNode()
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
                    JsonNode jsonVal = toJson(propVal)
                    if (jsonVal != null && !jsonVal.isNull() && jsonVal.size() > 0) {
                        log.debug "returned value is of class ${jsonVal.class?.name}"
                        jsonObj.set(jsonName, jsonVal)
                    }
                }
            }
        }
        jsonObj
    }

    static String toString(Object o) {
        if (o instanceof JsonNode) {
            try {
                return MAPPER.writeValueAsString(o)
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize JsonNode to string, falling back to toString()", e)
                return o.toString()
            }
        }
        return o?.toString()
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

    static JsonNode toJson(Object obj) {
        if (isNull(obj)) {
            return MAPPER.nullNode()
        }
        if (obj instanceof JsonNode) {
            return (JsonNode) obj
        }
        if (obj instanceof Date) {
            return MAPPER.getNodeFactory().textNode(DateUtil.dateToString((Date) obj))
        }
        if (obj instanceof Map) {
            return toJsonObject((Map)obj)
        }
        if (obj instanceof Iterable) {
            return toJsonArray((Iterable)obj)
        }
        if (obj instanceof GroovyObject) {
            return groovyObjectToJson((GroovyObject)obj)
        }
        return MAPPER.valueToTree(obj)
    }

    static ObjectNode toJsonObject(Map obj) {
        if (obj instanceof ObjectNode) {
            return (ObjectNode) obj
        }
        ObjectNode json = MAPPER.createObjectNode()
        obj?.each { key, val ->
            json.set("${key}", toJson(val))
        }
        return json
    }

    static ArrayNode toJsonArray(Iterable obj) {
        if (obj instanceof ArrayNode) {
            return (ArrayNode) obj
        }
        ArrayNode json = MAPPER.createArrayNode()
        obj?.each { val ->
            json.add(toJson(val))
        }
        return json
    }

    static ObjectNode removeNulls(ObjectNode json) {
        ObjectNode clean = MAPPER.createObjectNode()
        json.fields().each { Map.Entry<String, JsonNode> entry ->
            String key = entry.getKey()
            JsonNode val = entry.getValue()
            if (val.isObject()) {
                clean.set(key, removeNulls((ObjectNode)val))
            } else if (!val.isNull()) {
                clean.set(key, val)
            }
        }
        clean
    }
}
