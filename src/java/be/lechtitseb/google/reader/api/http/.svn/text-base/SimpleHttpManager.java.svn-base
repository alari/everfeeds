package be.lechtitseb.google.reader.api.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import be.lechtitseb.google.reader.api.core.Constants;
import be.lechtitseb.google.reader.api.model.authentication.ProxyCredentials;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;

/**
 * Helper class that executes HTTP GET/POST requests based on Apache Commons
 * HttpClient
 */
public class SimpleHttpManager implements HttpManager {
	private static final Logger LOG =
			Logger.getLogger(SimpleHttpManager.class.getName());
	protected List<Cookie> cookies = null;
	protected HttpMethodRetryHandler retryHandler = null;
	protected HttpClient httpClient = null;
	protected HttpConnectionManager manager = null;
	private String auth = null;

	public SimpleHttpManager(ProxyCredentials proxyCredentials) {
		manager = new MultiThreadedHttpConnectionManager();
		cookies = new ArrayList<Cookie>();
		httpClient = getHttpClient(proxyCredentials);
		retryHandler = getRetryHandler();
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}

	public void addCookie(Cookie cookie) {
		if (cookie != null) {
			cookies.add(cookie);
		} else {
			LOG.debug("Some psycho tried to add a null cookie to the list");
		}
	}

	public void clearCookies() {
		cookies.clear();
	}

	private byte[] download(HttpMethod method, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		method.setQueryString(toNameValuePairArray(parameters));
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				retryHandler);
		if (useCookies) {
			HttpState initialState = new HttpState();
			for (Cookie c : cookies) {
				initialState.addCookie(c);
			}
			httpClient.setState(initialState);
		}
		try {
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				String error =
						String.format("Expected 200 OK. Received %d %s",
								statusCode, HttpStatus
										.getStatusText(statusCode));
				throw new GoogleReaderException(error);
			}
			InputStream stream = method.getResponseBodyAsStream();
			if (stream == null) {
				throw new GoogleReaderException(
						"Expected response content, got null");
			}
			byte[] returnValue = getBytes(stream);
			return returnValue;
		} catch (Throwable error) {
			throw new GoogleReaderException(error);
		} finally {
			method.releaseConnection();
		}
	}

	public byte[] download(String url) throws GoogleReaderException {
		return download(new GetMethod(url), null, false);
	}

	public byte[] download(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return download(new GetMethod(url), parameters, false);
	}

	public byte[] download(String url, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		return download(new GetMethod(url), parameters, useCookies);
	}

	private String execute(HttpMethod method, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		method.setQueryString(toNameValuePairArray(parameters));
		method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				retryHandler);
		if (auth != null) {
			method.addRequestHeader(Constants.AUTHORIZATION_HTTP_HEADER,Constants.GOOGLE_AUTH_KEY+auth);
		}
		if (useCookies) {
			HttpState initialState = new HttpState();
			for (Cookie c : cookies) {
				initialState.addCookie(c);
			}
			httpClient.setState(initialState);
		}
		try {
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				String error =
						String.format("Expected 200 OK. Received %d %s",
								statusCode, HttpStatus
										.getStatusText(statusCode));
				throw new GoogleReaderException(error);
			}
			InputStream stream = method.getResponseBodyAsStream();
			if (stream == null) {
				throw new GoogleReaderException(
						"Expected response content, got null");
			}
			return getContent(stream);
		} catch (Throwable error) {
			throw new GoogleReaderException(error);
		} finally {
			method.releaseConnection();
		}
	}

	public String get(String url) throws GoogleReaderException {
		return get(url, null);
	}

	public String get(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return get(url, parameters, false);
	}

	public String get(String url, List<Parameter> parameters, boolean useCookies)
			throws GoogleReaderException {
		return execute(new GetMethod(url), parameters, useCookies);
	}

	private byte[] getBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
		return out.toByteArray();
	}

	private String getContent(InputStream in) throws IOException {
		InputStreamReader isreader =
				new InputStreamReader(in, Constants.HTTP_CHARSET_VALUE);
		StringBuilder sb = new StringBuilder();
		int ch;
		while ((ch = isreader.read()) > -1) {
			sb.append((char) ch);
		}
		in.close();
		return sb.toString();
	}

	protected HttpClient getHttpClient(ProxyCredentials proxyCredentials) {
		HttpClient client = new HttpClient(manager);
		client.getParams().setParameter(Constants.HTTP_CHARSET_PARAMETER,
				Constants.HTTP_CHARSET_VALUE);
		client.getParams().setParameter(Constants.USER_AGENT_PARAMETER,
				Constants.USER_AGENT_VALUE);
		HttpState initialState = new HttpState();
		for (Cookie c : cookies) {
			initialState.addCookie(c);
		}
		client.setState(initialState);
		
		if (proxyCredentials != null) {
			HostConfiguration config = client.getHostConfiguration();
			config.setProxy(proxyCredentials.getHost(), proxyCredentials.getPort());
			Credentials credentials = new UsernamePasswordCredentials(proxyCredentials.getLogin(), proxyCredentials.getPassword());
			AuthScope authScope = new AuthScope(proxyCredentials.getHost(), proxyCredentials.getPort());
			client.getState().setProxyCredentials(authScope, credentials);
		}
		return client;
	}

	protected HttpMethodRetryHandler getRetryHandler() {
		return new HttpMethodRetryHandler() {
			public boolean retryMethod(final HttpMethod method,
					final IOException exception, int executionCount) {
				if (executionCount >= 5) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					return true;
				}
				if (!method.isRequestSent()) {
					// Retry if the request has not been sent fully or
					// if it's OK to retry methods that have been sent
					return true;
				}
				if (exception instanceof SocketException) {
					return true;
				}
				// otherwise do not retry
				return false;
			}
		};
	}

	public String post(String url) throws GoogleReaderException {
		return post(url, null);
	}

	public String post(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return post(url, parameters, false);
	}

	public String post(String url, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		return execute(new PostMethod(url), parameters, useCookies);
	}

	private NameValuePair[] toNameValuePairArray(List<Parameter> in) {
		if (in == null) {
			return new NameValuePair[] {};
		}
		List<NameValuePair> out = new ArrayList<NameValuePair>();
		for (Parameter parameter : in) {
			if (parameter.hasName() && parameter.hasValue()) {
				out.add(new NameValuePair(parameter.getName(), parameter
						.getValue().toString()));
			}
		}
		return (NameValuePair[]) out.toArray(new NameValuePair[out.size()]);
	}
}