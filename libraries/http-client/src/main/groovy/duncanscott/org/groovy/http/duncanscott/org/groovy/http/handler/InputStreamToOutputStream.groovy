package duncanscott.org.groovy.http.duncanscott.org.groovy.http.handler

import com.google.common.io.ByteStreams

class InputStreamToOutputStream implements InputStreamHandler {

    final OutputStream outputStream

    InputStreamToOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream
    }

    void call(InputStream inputStream) {
        ByteStreams.copy(inputStream,outputStream)
    }

}
