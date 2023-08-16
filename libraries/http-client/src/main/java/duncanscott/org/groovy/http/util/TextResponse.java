package duncanscott.org.groovy.http.util;

import java.net.URI;
import java.util.Locale;

public class TextResponse {

    public String requestUri;
    public String requestMethod;

    public String text;
    public int statusCode;
    public String reasonPhrase;
    public Locale locale;
}
