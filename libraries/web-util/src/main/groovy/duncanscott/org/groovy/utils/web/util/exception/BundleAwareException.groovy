package duncanscott.org.groovy.utils.web.util.exception

import org.codehaus.groovy.runtime.StackTraceUtils

class BundleAwareException extends RuntimeException {

    Map messageBundleParams = [:]

    BundleAwareException() {}

    BundleAwareException(Throwable cause) { super(cause) }

    BundleAwareException(Map messageBundleParams) {
        super()
        if (messageBundleParams) {
            this.messageBundleParams.putAll(messageBundleParams)
        }
    }

    BundleAwareException(Map messageBundleParams, Throwable cause) {
        super(cause)
        if (messageBundleParams) {
            this.messageBundleParams.putAll(messageBundleParams)
        }
        if (!this.messageBundleParams['message']) {
            this.messageBundleParams['message'] = StackTraceUtils.extractRootCause(cause).message
        }
    }

    String getMessage() {
        if (messageBundleParams) {
            return (messageBundleParams['message']) ?: (messageBundleParams['code'])
        }
    }

    String setMessage(String message) {
        this.messageBundleParams['message'] = message
    }

    String toString() {
        StringBuilder msg = new StringBuilder()
        if (message) {
            msg << message
        }
        Map msgParams = new HashMap(messageBundleParams)
        msgParams.remove('message')
        if (msgParams) {
            msg << '['
            msg << msgParams.keySet().sort().collect { key ->
                "${key}:${msgParams[key]}"
            }.join(',')
            msg << ']'
        }
        if (cause) {
            msg << " (caused by ${cause})"
        }
        msg.toString()
    }

}
