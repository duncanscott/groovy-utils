package duncanscott.org.groovy.utils.http.client.handler

import com.google.common.io.ByteStreams
import duncanscott.org.groovy.utils.http.client.InputStreamHandler

class InputStreamToOutputStream implements InputStreamHandler {

    final OutputStream outputStream

    InputStreamToOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream
    }

    void call(InputStream inputStream) {
        ByteStreams.copy(inputStream, outputStream)
    }

}
