package be.lechtitseb.google.reader.api.model.authentication;

import be.lechtitseb.google.reader.api.model.exception.AuthenticationException;

/**
 * Interface to manage credentials
 */
public interface AuthenticationManager<T> {
	/**
	 * Get the current credentials if any
	 * 
	 * @return The current credentials or null if not set
	 */
	public T getCredentials();

	/**
	 * Removes any previously set credentials / cookie and sets the new
	 * credentials to use (login is NOT automatic)
	 * 
	 * @param credentials
	 *        The new credentials
	 */
	public void setCredentials(T credentials);

	/**
	 * Check if the credentials are set
	 * 
	 * @return true if the credentials are set
	 */
	public boolean hasCredentials();

	/**
	 * Check if a user is currently authenticated
	 * 
	 * @return True if authenticated
	 */
	public boolean isAuthenticated();

	/**
	 * Authenticate using the current credentials. Initializes the cookie that
	 * is used by subsequent requests. If already authenticated, nothing happens
	 * and true is returned
	 * 
	 * @return true if authentication succeeded or if already authenticated
	 * @throws AuthenticationException
	 *         If the authentication fails
	 */
	public boolean login() throws AuthenticationException;

	/**
	 * Clears authentication if logged in
	 */
	public void logout();

	/**
	 * Remove the current credentials if any
	 */
	public void clearCredentials();
}
