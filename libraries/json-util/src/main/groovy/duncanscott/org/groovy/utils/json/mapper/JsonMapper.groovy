package duncanscott.org.groovy.utils.json.mapper

import duncanscott.org.groovy.utils.ondemandcache.OnDemandCacheMapped

class JsonMapper<K extends JsonMapper> {

    //these are elements (Map or List) under keys that are not mapped to objects.
    OnDemandCacheMapped<String, Object> cachedSections = new OnDemandCacheMapped<>(false)

    private final OnDemandCacheMapped<String, Map<String, JsonMapper>> cachedChildObjects = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String, List<JsonMapper>> cachedChildArrays = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String, JsonMapper> cachedChildren = new OnDemandCacheMapped<>()

    private final List<MappingError> mappingErrors = []
    final List<JsonMapper> children = []

    Map<String, Object> json
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

    JsonMapper(Map<String, Object> json) {
        this.parent = null
        this.key = null
        this.json = json
        this.level = 1
    }

    JsonMapper(Map<String, Object> json, K parent, String key) {
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
            Map<String, Object> subJson = objectSection(key)
            subJson.each { String k, Object childJson ->
                if (childJson instanceof Map) {
                    JsonMapper childMapper = clazz.newInstance()
                    childMapper.json = (Map<String, Object>) childJson
                    childMapper.setParent(this, key)
                    keyChild[k] = childMapper
                }
            }
            keyChild
        }
    }

    List<JsonMapper> childElements(String key, Class<JsonMapper> clazz) {
        cachedChildArrays.fetch(key) {
            Integer arrayIndex = 0
            arraySection(key)?.collect { Object childJson ->
                if (childJson instanceof Map) {
                    JsonMapper childMapper = clazz.newInstance()
                    childMapper.json = (Map<String, Object>) childJson
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
            Map<String, Object> childJson = objectSection(key, createIfAbsent)
            if (childJson != null) {
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this, key)
                return childMapper
            }
            return null
        }
    }

    Map<String, Object> objectSection(String key, boolean create = false) {
        (Map<String, Object>) cachedSections.fetch(key) {
            Object existingSection = json[key]
            if (existingSection instanceof Map) {
                return existingSection
            }
            if (create) {
                Map<String, Object> newSection = new LinkedHashMap<>()
                json[key] = newSection
                return newSection
            }
            return null
        }
    }


    List<Object> arraySection(String key, boolean create = false) {
        (List<Object>) cachedSections.fetch(key) {
            Object existingSection = json[key]
            if (existingSection instanceof List) {
                return existingSection
            }
            if (create) {
                List<Object> newSection = new ArrayList<>()
                json[key] = newSection
                return newSection
            }
            return null
        }
    }

    Map<String, Object> createObject(String key) {
        objectSection(key, true)
    }

    Map<String, Object> getObject(String key) {
        objectSection(key, false)
    }

    List<Object> createArray(String key) {
        arraySection(key, true)
    }

    List<Object> getArray(String key) {
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
