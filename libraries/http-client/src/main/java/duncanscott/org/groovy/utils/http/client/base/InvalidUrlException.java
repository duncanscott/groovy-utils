package duncanscott.org.groovy.utils.http.client.base;

public class InvalidUrlException extends RuntimeException {
    private final String url;

    public InvalidUrlException(final String url) {
        this(url, "invalid URL [" + url + "]");
    }

    public InvalidUrlException(String url, String message) {
        super(message);
        this.url = url;
    }

    public final String getUrl() {
        return url;
    }
}
