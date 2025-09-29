package duncanscott.org.groovy.utils.http.client.handler

import duncanscott.org.groovy.utils.http.client.InputStreamHandler

class InputStreamToClosure implements InputStreamHandler {

    final Closure closure

    InputStreamToClosure(Closure closure) {
        this.closure = closure
    }

    void call(InputStream inputStream) {
        closure.call(inputStream)
    }

}
