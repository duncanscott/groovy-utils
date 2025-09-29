package duncanscott.org.groovy.http.client

import org.apache.hc.core5.http.ClassicHttpRequest

interface AfterRequestInterceptor<K extends Response>  {

    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse)
}
