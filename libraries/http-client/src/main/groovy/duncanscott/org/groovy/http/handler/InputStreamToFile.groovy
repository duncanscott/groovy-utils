package duncanscott.org.groovy.http.handler

import com.google.common.io.ByteStreams
import duncanscott.org.groovy.http.client.InputStreamHandler

class InputStreamToFile implements InputStreamHandler {

    final FileOutputStream outputStream

    InputStreamToFile(File file) {
        this.outputStream = new FileOutputStream(file)
    }

    void call(InputStream inputStream) {
        ByteStreams.copy(inputStream, outputStream)
    }

}
