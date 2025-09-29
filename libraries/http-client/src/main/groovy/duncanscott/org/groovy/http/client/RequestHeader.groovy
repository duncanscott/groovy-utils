package duncanscott.org.groovy.http.client

class RequestHeader {

    final String name
    final String value

    RequestHeader(String name, String value) {
        this.name = name
        this.value = value
    }

}
