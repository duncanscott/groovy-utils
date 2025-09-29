package duncanscott.org.groovy.utils.http.client.base;

import org.apache.hc.core5.http.ClassicHttpRequest;

public interface AfterRequestInterceptor<K extends HttpClientResponse> {
    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse);
}
