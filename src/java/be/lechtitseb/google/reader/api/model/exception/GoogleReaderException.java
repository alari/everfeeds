package be.lechtitseb.google.reader.api.model.exception;

public class GoogleReaderException extends Exception{
	public GoogleReaderException(String s, Throwable t) {
		super(s,t);
	}
	
	public GoogleReaderException(Throwable t) {
		super(t);
	}
	
	public GoogleReaderException(String s) {
		super(s);
	}
}
