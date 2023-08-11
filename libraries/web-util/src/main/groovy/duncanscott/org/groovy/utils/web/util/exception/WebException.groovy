package duncanscott.org.groovy.utils.web.util.exception

class WebException extends BundleAwareException implements HttpStatusCodeReporter {

    private final Integer status

    WebException(Integer status) {
        super()
        this.status = status
    }

    WebException() {
        this(500)
    }

    WebException(Throwable cause) {
        super(cause)
        this.status = 500
    }

    WebException(Integer status, Throwable cause) {
        super(cause)
        this.status = status
    }

    WebException(Map messageBundleParams, Integer status) {
        super(messageBundleParams)
        this.status = status
    }

    WebException(Object message, Integer status) {
        setMessage(message as String)
        this.status = status
    }

    WebException(Map messageBundleParams, Integer status, Throwable cause) {
        super(messageBundleParams, cause)
        this.status = status
    }

    WebException(Object message, Integer status, Throwable cause) {
        super(cause)
        setMessage(message as String)
        this.status = status
    }

    Integer getHttpStatusCode() {
        return status
    }

}
