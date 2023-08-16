package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util

class InvalidUrlException extends RuntimeException {

    final String url

    InvalidUrlException(String url) {
        this(url, "invalid URL [${url}]")
    }

    InvalidUrlException(String url, String message) {
        super(message)
        this.url = url
    }
}
