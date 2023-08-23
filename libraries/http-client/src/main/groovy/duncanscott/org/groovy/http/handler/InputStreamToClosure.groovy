package duncanscott.org.groovy.http.handler

import duncanscott.org.groovy.javautil.http.InputStreamHandler

class InputStreamToClosure implements InputStreamHandler {

    final Closure closure

    InputStreamToClosure(Closure closure) {
        this.closure = closure
    }

    void call(InputStream inputStream) {
        closure.call(inputStream)
    }

}
