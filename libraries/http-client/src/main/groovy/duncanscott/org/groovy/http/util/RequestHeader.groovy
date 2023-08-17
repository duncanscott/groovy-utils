package duncanscott.org.groovy.http.util

class RequestHeader {

    final String name
    final String value

    RequestHeader(String name, String value) {
        this.name = name
        this.value = value
    }

}
