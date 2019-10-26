package duncanscott.org.groovy.utils.web.util.exception

class MethodNotAllowedException extends WebException {
	
	MethodNotAllowedException() {
		super([code: 'http.method.not.allowed'], 405)
	}
}
