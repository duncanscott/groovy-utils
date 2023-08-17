package duncanscott.org.groovy.http.handler

class InputStreamToString implements InputStreamHandler {

    String text

    synchronized void call(InputStream inputStream) {
        text = "${inputStream}"
    }

    String toString() {
        text
    }

}
