package duncanscott.org.groovy.utils.enumutil

import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache

class EnumConverter<K extends Enum> {

    private final OnDemandCache<Map<String, K>> cachedConversionMap = new OnDemandCache<>()
    private final Class<K> enumType
    final boolean caseSensitive

    EnumConverter(Class<K> enumType, boolean caseSensitive) {
        this.enumType = enumType
        this.caseSensitive = caseSensitive
    }

    EnumConverter(Class<K> enumType) {
        this.enumType = enumType
        this.caseSensitive = true
    }

    private Map<String, K> getConversionMap() {
        return cachedConversionMap.fetch {
            Map<String, K> cMap = [:]
            enumType.values().each { K enumValue ->
                String strVal = caseSensitive ? enumValue.toString().trim() : enumValue.toString().trim().toLowerCase()
                if (cMap.containsKey(strVal)) {
                    throw new RuntimeException("duplicate ${caseSensitive ? 'case-sensitive' : 'case-insensitive'} enum value [${strVal}]")
                }
                cMap[strVal] = enumValue
            }
            cMap
        }
    }

    K toEnum(Object value) {
        String trimValue = caseSensitive ? value?.toString()?.trim() : value?.toString()?.trim()?.toLowerCase()
        K matchingType = null
        if (trimValue) {
            matchingType = conversionMap[trimValue]
        }
        matchingType
    }

}
