package duncanscott.org.groovy.http.client;

import org.apache.hc.core5.http.ClassicHttpRequest;

public interface AfterRequestInterceptor<K extends HttpClientResponse> {
    public abstract void afterRequest(ClassicHttpRequest httpRequest, K httpResponse);
}
