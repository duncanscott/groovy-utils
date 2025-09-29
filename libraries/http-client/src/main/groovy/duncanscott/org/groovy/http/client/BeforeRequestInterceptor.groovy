package duncanscott.org.groovy.http.client

import org.apache.hc.core5.http.ClassicHttpRequest

interface BeforeRequestInterceptor {

    void beforeRequest(ClassicHttpRequest httpRequest)
}