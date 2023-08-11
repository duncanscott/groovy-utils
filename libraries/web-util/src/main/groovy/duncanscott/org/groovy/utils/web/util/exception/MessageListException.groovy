package duncanscott.org.groovy.utils.web.util.exception


class MessageListException extends RuntimeException implements HttpStatusCodeReporter {

    private List<Map> messages = []
    private Integer status

    MessageListException() {
        super()
    }

    MessageListException(Throwable cause) {
        super(cause)
    }

    MessageListException(List<Map> messages, Integer status = null) {
        super()
        addMessages(messages)
        this.status = status
    }

    MessageListException(Map message, Integer status = null) {
        super()
        addMessage(message)
        this.status = status
    }

    MessageListException(BundleAwareException bae, Integer status = null) {
        super()
        addBundleAwareException(bae)
        this.status = status
    }

    MessageListException(List<Map> messages, String message, Integer status = null) {
        super(message)
        addMessages(messages)
        this.status = status
    }


    void addBundleAwareException(BundleAwareException bae) {
        addMessage(bae?.messageBundleParams)
    }

    void setHttpStatusCode(Integer statusCode) {
        this.status = statusCode
    }

    Integer getHttpStatusCode() {
        return status
    }

    List<Map> getMessages() {
        return messages
    }

    void addMessage(Map message) {
        if (message != null) {
            messages.add(message)
        }
    }

    void addMessages(List<Map> messages) {
        messages?.each { Map message -> addMessage(message) }
    }

    String toString() {
        StringBuilder msg = new StringBuilder()
        if (message) {
            msg << message
        }
        if (messages) {
            messages.each { Map msgParams ->
                msg << '['
                msg << msgParams.keySet().sort().collect { key ->
                    "${key}:${msgParams[key]}"
                }.join(',')
                msg << ']'
            }
        }
        if (cause) {
            msg << " (caused by ${cause})"
        }
        msg.toString()
    }


}
