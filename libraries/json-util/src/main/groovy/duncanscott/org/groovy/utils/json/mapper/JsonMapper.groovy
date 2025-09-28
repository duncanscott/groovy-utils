package duncanscott.org.groovy.utils.json.mapper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCacheMapped

class JsonMapper<K extends JsonMapper> {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    //these are elements (ObjectNode or ArrayNode) under keys that are not mapped to objects.
    OnDemandCacheMapped<String, JsonNode> cachedSections = new OnDemandCacheMapped<>(false)

    private final OnDemandCacheMapped<String, Map<String, JsonMapper>> cachedChildObjects = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String, List<JsonMapper>> cachedChildArrays = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String, JsonMapper> cachedChildren = new OnDemandCacheMapped<>()

    private final List<MappingError> mappingErrors = []
    final List<JsonMapper> children = []

    ObjectNode json
    Integer level //depth in object
    Integer index //index in array
    K parent
    String key

    JsonMapper() {
        this.parent = null
        this.key = null
        this.json = null
        this.level = 1
    }

    JsonMapper(ObjectNode json) {
        this.parent = null
        this.key = null
        this.json = json
        this.level = 1
    }

    JsonMapper(ObjectNode json, K parent, String key) {
        this.json = json
        setParent(parent, key)
    }

    void setParent(K parent, String key) {
        this.parent = parent
        this.level = parent.level + 1
        this.parent.children << this
        this.key = key
    }

    List<MappingError> getMappingErrors() {
        List<MappingError> errors = []
        errors.addAll(this.mappingErrors)
        children.each { JsonMapper child ->
            errors.addAll(child.mappingErrors)
        }
        errors.sort()
    }

    void addError(message) {
        mappingErrors << new MappingError(this, message)
    }

    Map<String, JsonMapper> childObjects(String key, Class<JsonMapper> clazz) {
        cachedChildObjects.fetch(key) {
            Map<String, JsonMapper> keyChild = [:]
            ObjectNode subJson = objectSection(key)
            subJson?.fields()?.each { Map.Entry<String, JsonNode> entry ->
                if (entry.getValue().isObject()) {
                    JsonMapper childMapper = clazz.newInstance()
                    childMapper.json = (ObjectNode) entry.getValue()
                    childMapper.setParent(this, key)
                    keyChild[entry.getKey()] = childMapper
                }
            }
            keyChild
        }
    }

    List<JsonMapper> childElements(String key, Class<JsonMapper> clazz) {
        cachedChildArrays.fetch(key) {
            Integer arrayIndex = 0
            arraySection(key)?.collect { JsonNode childJson ->
                if (childJson.isObject()) {
                    JsonMapper childMapper = clazz.newInstance()
                    childMapper.json = (ObjectNode) childJson
                    childMapper.setParent(this, key)
                    childMapper.index = arrayIndex++
                    return childMapper
                }
                return null
            }.findAll { it != null }
        }
    }

    JsonMapper childElement(String key, Class<JsonMapper> clazz, boolean createIfAbsent = false) {
        cachedChildren.fetch(key) {
            ObjectNode childJson = objectSection(key, createIfAbsent)
            if (childJson != null) {
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this, key)
                return childMapper
            }
            return null
        }
    }

    ObjectNode objectSection(String key, boolean create = false) {
        (ObjectNode) cachedSections.fetch(key) {
            JsonNode existingSection = json.get(key)
            if (existingSection instanceof ObjectNode) {
                return existingSection
            }
            if (create) {
                ObjectNode newSection = MAPPER.createObjectNode()
                json.set(key, newSection)
                return newSection
            }
            return null
        }
    }


    ArrayNode arraySection(String key, boolean create = false) {
        (ArrayNode) cachedSections.fetch(key) {
            JsonNode existingSection = json.get(key)
            if (existingSection instanceof ArrayNode) {
                return existingSection
            }
            if (create) {
                ArrayNode newSection = MAPPER.createArrayNode()
                json.set(key, newSection)
                return newSection
            }
            return null
        }
    }

    ObjectNode createObject(String key) {
        objectSection(key, true)
    }

    ObjectNode getObject(String key) {
        objectSection(key, false)
    }

    ArrayNode createArray(String key) {
        arraySection(key, true)
    }

    ArrayNode getArray(String key) {
        arraySection(key, false)
    }

    List<String> getKeyChain() {
        if (parent) {
            List<String> chain = new ArrayList<String>(parent.keyChain)
            chain.add(key)
            return chain
        }
        return new ArrayList<String>()
    }

    String getObjectId() {
        return null
    }

    String getObjectName() {
        if (key) {
            if (index != null) {
                return "${key}/${index}"
            } else {
                return "${key}"
            }
        } else {
            if (objectId) {
                return "#${objectId}"
            } else {
                return '#'
            }
        }

    }

    String toString() {
        if (parent) {
            return "${parent}/${objectName}"
        } else {
            return objectName
        }
    }
}
