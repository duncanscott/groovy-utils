package duncanscott.org.groovy.http.handler

import duncanscott.org.groovy.http.client.InputStreamHandler

class InputStreamToString implements InputStreamHandler {

    String text

    synchronized void call(InputStream inputStream) {
        text = "${inputStream}"
    }

    String toString() {
        text
    }

}
