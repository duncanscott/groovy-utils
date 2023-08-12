package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util

import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.HttpStatus

class HttpResponse {

    TextResponse textResponse

    boolean getSuccess() {
        statusCode in [HttpStatus.SC_CREATED, HttpStatus.SC_OK]
    }

    String getRequestUri() {
        textResponse?.requestUri
    }

    String getRequestMethod() {
        textResponse?.requestMethod
    }

    String getText() {
        textResponse?.text
    }

    int getStatusCode() {
        textResponse ? textResponse.statusCode : 0
    }

    String getReasonPhrase() {
        textResponse?.reasonPhrase
    }

    Locale getLocal() {
        textResponse?.locale
    }

    String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.class.simpleName)
        stringBuilder.append('(')
        if (requestUri) {
            stringBuilder.append(requestUri)
            stringBuilder.append(':' + statusCode)
            if (reasonPhrase) {
                stringBuilder.append(':' + reasonPhrase)
            }
        }
        stringBuilder.append(')')
        stringBuilder.toString()
    }

}
