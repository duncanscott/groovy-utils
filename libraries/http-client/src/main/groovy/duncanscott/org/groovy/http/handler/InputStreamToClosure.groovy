package duncanscott.org.groovy.http.handler

class InputStreamToClosure implements InputStreamHandler {

    final Closure closure

    InputStreamToClosure(Closure closure) {
        this.closure = closure
    }

    void call(InputStream inputStream) {
        closure.call(inputStream)
    }

}
