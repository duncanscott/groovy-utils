package duncanscott.org.groovy.utils.enumutil

class EnumConverter<K extends Enum> {

    private final Map<String, K> conversionMap = new HashMap<>()

    private final Class<K> enumType
    final boolean caseSensitive

    EnumConverter(Class<K> enumType, boolean caseSensitive) {
        this.enumType = enumType
        this.caseSensitive = caseSensitive
        populateConversionMap()
    }

    EnumConverter(Class<K> enumType) {
        this(enumType, true)
    }

    private void populateConversionMap() {
        enumType.values().each { K enumValue ->
            String strVal = caseSensitive ? enumValue.toString().trim() : enumValue.toString().trim().toLowerCase()
            if (conversionMap.containsKey(strVal)) {
                throw new RuntimeException("duplicate ${caseSensitive ? 'case-sensitive' : 'case-insensitive'} enum value [${strVal}]")
            }
            conversionMap[strVal] = enumValue
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
