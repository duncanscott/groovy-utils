package duncanscott.org.groovy.http.handler

import com.google.common.io.ByteStreams
import duncanscott.org.groovy.javautil.http.InputStreamHandler

class InputStreamToOutputStream implements InputStreamHandler {

    final OutputStream outputStream

    InputStreamToOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream
    }

    void call(InputStream inputStream) {
        ByteStreams.copy(inputStream, outputStream)
    }

}
