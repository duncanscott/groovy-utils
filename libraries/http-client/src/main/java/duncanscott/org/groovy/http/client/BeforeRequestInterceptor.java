package duncanscott.org.groovy.http.client;

import org.apache.hc.core5.http.ClassicHttpRequest;

public interface BeforeRequestInterceptor {
    public abstract void beforeRequest(ClassicHttpRequest httpRequest);
}
