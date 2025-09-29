package duncanscott.org.groovy.utils.http.client.handler

import com.google.common.io.ByteStreams
import duncanscott.org.groovy.utils.http.client.InputStreamHandler

class InputStreamToFile implements InputStreamHandler {

    final FileOutputStream outputStream

    InputStreamToFile(File file) {
        this.outputStream = new FileOutputStream(file)
    }

    void call(InputStream inputStream) {
        ByteStreams.copy(inputStream, outputStream)
    }

}
