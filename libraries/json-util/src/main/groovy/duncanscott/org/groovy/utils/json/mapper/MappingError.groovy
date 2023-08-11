package duncanscott.org.groovy.utils.json.mapper

class MappingError implements Comparable<MappingError> {

    final JsonMapper element
    final Object message

    MappingError(JsonMapper element, message) {
        this.element = element
        this.message = message
    }

    @Override
    int compareTo(MappingError o) {
        int result = this.element.level <=> o.element.level
        if (!result) {
            result = this.class.simpleName.compareToIgnoreCase(o.class.simpleName)
        }
        if (!result) {
            try {
                result = this.message <=> o.message
            } catch (ignore) {
            }
        }
        result
    }

    String toString() {
        "${element}: ${message}"
    }
}
