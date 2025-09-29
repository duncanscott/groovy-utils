package duncanscott.org.groovy.utils.http.client;

import org.apache.hc.core5.http.ClassicHttpRequest;

public interface BeforeRequestInterceptor {
    public abstract void beforeRequest(ClassicHttpRequest httpRequest);
}
