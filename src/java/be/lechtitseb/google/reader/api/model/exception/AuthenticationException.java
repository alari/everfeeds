package be.lechtitseb.google.reader.api.model.exception;

public class AuthenticationException extends Exception {
	public AuthenticationException(String s, Throwable t) {
		super(s, t);
	}

	public AuthenticationException(Throwable t) {
		super(t);
	}

	public AuthenticationException(String s) {
	}
}
