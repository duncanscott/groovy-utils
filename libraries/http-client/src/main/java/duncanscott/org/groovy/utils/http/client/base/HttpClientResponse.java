package duncanscott.org.groovy.utils.http.client.base;

import org.apache.hc.core5.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HttpClientResponse {

    List<Integer> successCodes = Arrays.asList(
            HttpStatus.SC_CREATED,
            HttpStatus.SC_OK
    );
    private TextResponse textResponse;

    public boolean getSuccess() {
        return successCodes.contains(getStatusCode());
    }

    public String getRequestUri() {
        final TextResponse response = textResponse;
        return (response == null ? null : response.requestUri);
    }

    public String getRequestMethod() {
        final TextResponse response = textResponse;
        return (response == null ? null : response.requestMethod);
    }

    public String getText() {
        final TextResponse response = textResponse;
        return (response == null ? null : response.text);
    }

    public int getStatusCode() {
        return (textResponse != null) ? textResponse.statusCode : 0;
    }

    public String getReasonPhrase() {
        final TextResponse response = textResponse;
        return (response == null ? null : response.reasonPhrase);
    }

    public Locale getLocal() {
        final TextResponse response = textResponse;
        return (response == null ? null : response.locale);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.getClass().getSimpleName());
        stringBuilder.append("(");
        if (getRequestUri() != null && !getRequestUri().isEmpty()) {
            stringBuilder.append(getRequestUri());
            stringBuilder.append(":").append(getStatusCode());
            if (getReasonPhrase() != null && !getReasonPhrase().isEmpty()) {
                stringBuilder.append(":").append(getReasonPhrase());
            }

        }

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public TextResponse getTextResponse() {
        return textResponse;
    }

    public void setTextResponse(TextResponse textResponse) {
        this.textResponse = textResponse;
    }
}
