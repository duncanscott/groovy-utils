package duncanscott.org.groovy.utils.enumutil;

import java.util.HashMap;
import java.util.Map;

public class EnumConverter<K extends Enum<K>> {

    private final Map<String, K> conversionMap = new HashMap<>();
    private final Class<K> enumType;
    public final boolean caseSensitive;

    public EnumConverter(Class<K> enumType, boolean caseSensitive) {
        this.enumType = enumType;
        this.caseSensitive = caseSensitive;
        populateConversionMap();
    }

    public EnumConverter(Class<K> enumType) {
        this(enumType, true);
    }

    private void populateConversionMap() {
        for (K enumValue : enumType.getEnumConstants()) {
            String strVal = enumValue.toString().trim();
            if (!caseSensitive) {
                strVal = strVal.toLowerCase();
            }
            if (conversionMap.containsKey(strVal)) {
                throw new RuntimeException("duplicate " + (caseSensitive ? "case-sensitive" : "case-insensitive") + " enum value [" + strVal + "]");
            }
            conversionMap.put(strVal, enumValue);
        }
    }

    public K toEnum(Object value) {
        if (value == null) {
            return null;
        }
        String trimValue = value.toString().trim();
        if (!caseSensitive) {
            trimValue = trimValue.toLowerCase();
        }
        return conversionMap.get(trimValue);
    }
}
