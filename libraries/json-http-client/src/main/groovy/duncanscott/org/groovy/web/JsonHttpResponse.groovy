package duncanscott.org.groovy.web

import org.apache.http.HttpStatus
import org.apache.http.ProtocolVersion
import org.apache.http.StatusLine
import org.json.simple.JSONObject

class JsonHttpResponse {

    boolean getSuccess() {
        statusLine?.statusCode in [HttpStatus.SC_CREATED, HttpStatus.SC_OK]
    }

    JSONObject json
    StatusLine statusLine
    URI uri

    int getStatusCode() {
        statusLine?.statusCode
    }

    ProtocolVersion getProtocolVersion() {
        statusLine?.protocolVersion
    }

    String getReasonPhrase() {
        statusLine?.reasonPhrase
    }

    String toString() {
        if (uri) {
            if (statusLine) {
                return "${uri}(${statusLine})"
            } else {
                return "${uri}"
            }
        }
        return "${this.class.simpleName}[${super}]"
    }

}
