package duncanscott.org.groovy.utils.http.client.base;

import org.apache.hc.core5.http.ClassicHttpRequest;

public interface BeforeRequestInterceptor {
    public abstract void beforeRequest(ClassicHttpRequest httpRequest);
}
