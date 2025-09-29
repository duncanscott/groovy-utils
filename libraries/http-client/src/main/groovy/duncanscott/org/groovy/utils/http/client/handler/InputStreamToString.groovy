package duncanscott.org.groovy.utils.http.client.handler

import duncanscott.org.groovy.utils.http.client.base.InputStreamHandler

class InputStreamToString implements InputStreamHandler {

    String text

    synchronized void call(InputStream inputStream) {
        text = "${inputStream}"
    }

    String toString() {
        text
    }

}
