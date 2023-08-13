package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util

import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.HttpStatus

class HttpResponse {

    @Delegate TextResponse textResponse

    boolean getSuccess() {
        textResponse?.statusCode in [HttpStatus.SC_CREATED, HttpStatus.SC_OK]
    }

    String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.class.simpleName)
        stringBuilder.append('(')
        if (textResponse?.requestUri) {
            stringBuilder.append(textResponse?.requestUri)
            stringBuilder.append(':' + textResponse.statusCode)
            if (reasonPhrase) {
                stringBuilder.append(':' + textResponse.reasonPhrase)
            }
        }
        stringBuilder.append(')')
        stringBuilder.toString()
    }

}
