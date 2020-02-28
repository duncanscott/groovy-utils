package duncanscott.org.groovy.utils.json.mapper

import duncanscott.org.groovy.utils.ondemandcache.OnDemandCacheMapped
import org.json.simple.JSONArray
import org.json.simple.JSONAware
import org.json.simple.JSONObject


class JsonMapper<K extends JsonMapper> {

    //these are elements (JSONObject or JSONArray) under keys that are not mapped to objects.
    OnDemandCacheMapped<String, JSONAware> cachedSections = new OnDemandCacheMapped<>(false)

    private final OnDemandCacheMapped<String, Map<String,JsonMapper>> cachedChildObjects = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String,List<JsonMapper>> cachedChildArrays = new OnDemandCacheMapped<>()
    private final OnDemandCacheMapped<String,JsonMapper> cachedChildren = new OnDemandCacheMapped<>()

    private final List<MappingError> mappingErrors = []
    final List<JsonMapper> children = []

    JSONObject json
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

    JsonMapper(JSONObject json) {
        this.parent = null
        this.key = null
        this.json = json
        this.level = 1
    }

    JsonMapper(JSONObject json, K parent, String key) {
        this.json = json
        setParent(parent,key)
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
        mappingErrors << new MappingError(this,message)
    }

    Map<String,JsonMapper> childObjects(String key, Class<JsonMapper> clazz) {
        cachedChildObjects.fetch(key) {
            Map<String,JsonMapper> keyChild = [:]
            JSONObject subJson = objectSection(key)
            subJson.each { String k, JSONObject childJson ->
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this,key)
                keyChild[k] = childMapper
            }
            keyChild
        }
    }

    List<JsonMapper> childElements(String key, Class<JsonMapper> clazz) {
        cachedChildArrays.fetch(key) {
            Integer arrayIndex = 0
            arraySection(key)?.collect { JSONObject childJson ->
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this,key)
                childMapper.index = arrayIndex++
                return childMapper
            }
        }
    }

    JsonMapper childElement(String key, Class<JsonMapper> clazz, boolean createIfAbsent = false) {
        cachedChildren.fetch(key) {
            JSONObject childJson = objectSection(key, createIfAbsent)
            if (childJson != null) {
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this,key)
                return childMapper
            }
            return null
        }
    }

    JSONObject objectSection(String key, boolean create = false) {
        (JSONObject) cachedSections.fetch(key) {
            Object existingSection = json[key]
            if (existingSection instanceof JSONObject) {
                return existingSection
            }
            if (create) {
                JSONObject newSection = new JSONObject()
                json[key] = newSection
                return newSection
            }
            return null
        }
    }


    JSONArray arraySection(String key, boolean create = false) {
        (JSONArray) cachedSections.fetch(key) {
            Object existingSection = json[key]
            if (existingSection instanceof JSONArray) {
                return existingSection
            }
            if (create) {
                JSONArray newSection = new JSONArray()
                json[key] = newSection
                return newSection
            }
            return null
        }
    }

    JSONObject createObject(String key) {
        objectSection(key,true)
    }

    JSONObject getObject(String key) {
        objectSection(key,false)
    }

    JSONArray createArray(String key) {
        arraySection(key,true)
    }

    JSONArray getArray(String key) {
        arraySection(key,false)
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
            if (index) {
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
