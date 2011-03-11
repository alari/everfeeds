package be.lechtitseb.google.reader.api.http;

import java.util.List;

import org.apache.commons.httpclient.Cookie;

import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;

public interface HttpManager {
	/**
	 * Download content from the specified URL (HTTP GET)
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @return a byte array (data) containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	byte[] download(String url) throws GoogleReaderException;

	/**
	 * Download content from the specified URL (HTTP GET)
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP GET query string
	 * @return a byte array (data) containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	byte[] download(String url, List<Parameter> parameters)
			throws GoogleReaderException;

	/**
	 * Download content from the specified URL (HTTP GET)
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP GET query string
	 * @param useCookies
	 *        true if cookies should be sent with the request
	 * @return a byte array (data) containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	byte[] download(String url, List<Parameter> parameters, boolean useCookies)
			throws GoogleReaderException;

	/**
	 * Perform an HTTP GET request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @return a String containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	String get(String url) throws GoogleReaderException;

	/**
	 * Perform an HTTP GET request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP GET query string
	 * @return a String containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	String get(String url, List<Parameter> parameters)
			throws GoogleReaderException;

	/**
	 * Perform an HTTP GET request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP GET.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP GET query string
	 * @param useCookies
	 *        true if cookies should be sent with the request
	 * @return a String containing the body of the HTTP GET response.
	 * @throws GoogleReaderException
	 */
	String get(String url, List<Parameter> parameters, boolean useCookies)
			throws GoogleReaderException;

	/**
	 * Perform an HTTP POST request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP POST.
	 * @return a String containing the body of the HTTP POST response.
	 * @throws GoogleReaderException
	 */
	String post(String url) throws GoogleReaderException;

	/**
	 * Perform an HTTP POST request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP POST.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP POST query string
	 * @return a String containing the body of the HTTP POST response.
	 * @throws GoogleReaderException
	 */
	String post(String url, List<Parameter> parameters)
			throws GoogleReaderException;

	/**
	 * Perform an HTTP POST request to the specified URL.
	 * 
	 * @param url
	 *        the URL to HTTP POST.
	 * @param parameters
	 *        the key/value pairs to add to the HTTP POST query string
	 * @param useCookies
	 *        true if cookies should be sent with the request
	 * @return a String containing the body of the HTTP POST response.
	 * @throws GoogleReaderException
	 */
	String post(String url, List<Parameter> parameters, boolean useCookies)
			throws GoogleReaderException;

	void addCookie(Cookie cookie);
	
	/**
	 * Add Auth value for connect to google resource
	 * @param auth
	 */
	void setAuth(String auth);

	void clearCookies();
}