package duncanscott.org.groovy.utils.http.client.json


import duncanscott.org.groovy.utils.http.client.HttpClientImpl
import duncanscott.org.groovy.utils.http.client.RequestHeader
import org.apache.hc.core5.http.HttpHeaders

class JsonHttpClient extends HttpClientImpl<JsonHttpResponse> {

    JsonHttpClient() {
        super(JsonHttpResponse)
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/json'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/json'))
    }

}
