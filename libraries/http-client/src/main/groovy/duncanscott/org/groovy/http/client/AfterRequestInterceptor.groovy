package duncanscott.org.groovy.http.client

import org.apache.hc.core5.http.ClassicHttpRequest

interface AfterRequestInterceptor<K extends HttpClientResponse> {

    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse)
}
