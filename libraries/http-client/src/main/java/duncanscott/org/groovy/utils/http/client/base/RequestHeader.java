package duncanscott.org.groovy.utils.http.client.base;

public class RequestHeader {
    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public final String getName() {
        return name;
    }

    public final String getValue() {
        return value;
    }

    private final String name;
    private final String value;
}
