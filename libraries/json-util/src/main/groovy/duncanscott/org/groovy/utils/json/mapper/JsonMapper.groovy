package duncanscott.org.groovy.utils.json.mapper


import duncanscott.org.groovy.utils.ondemandcache.OnDemandCacheMapped
import org.json.simple.JSONObject


class JsonMapper<K extends JsonMapper> {

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

    List<JsonMapper> childElements(String key, Class<JsonMapper> clazz) {
        cachedChildArrays.fetch(key) { String k ->
            Integer arrayIndex = 0
            json[key]?.collect { JSONObject childJson ->
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this,key)
                childMapper.index = arrayIndex++
                return childMapper
            }
        }
    }

    JsonMapper childElement(String key, Class<JsonMapper> clazz) {
        cachedChildren.fetch(key) { String k ->
            Object childJson = json[key]
            if (childJson instanceof JSONObject) {
                JsonMapper childMapper = clazz.newInstance()
                childMapper.json = childJson
                childMapper.setParent(this,key)
                return childMapper
            }
            return null
        }
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
        if (index) {
            return index
        }
        return null
    }

    String getObjectName() {
        if (key) {
            if (objectId) {
                return "(${key})[${objectId}]"
            } else {
                return "(${key})"
            }
        } else {
            if (objectId) {
                return "[${objectId}]"
            } else {
                return ''
            }
        }

    }

    String toString() {
        if (parent) {
            return "${parent}${objectName}"
        } else {
            return objectName
        }
    }

}
