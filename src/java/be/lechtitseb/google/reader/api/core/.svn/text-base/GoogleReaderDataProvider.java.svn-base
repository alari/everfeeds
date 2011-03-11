package be.lechtitseb.google.reader.api.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;

import be.lechtitseb.google.reader.api.http.HttpManager;
import be.lechtitseb.google.reader.api.http.Parameter;
import be.lechtitseb.google.reader.api.http.SimpleHttpManager;
import be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager;
import be.lechtitseb.google.reader.api.model.authentication.GoogleCredentials;
import be.lechtitseb.google.reader.api.model.authentication.ProxyCredentials;
import be.lechtitseb.google.reader.api.model.exception.AuthenticationException;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import be.lechtitseb.google.reader.api.model.feed.FeedDescriptor;
import be.lechtitseb.google.reader.api.model.feed.ItemDescriptor;
import be.lechtitseb.google.reader.api.model.format.OutputFormat;
import org.json.JSONException;
import org.json.JSONObject;

//FIXME Most of the code isn't thread safe
//FIXME Lots of code duplication, I guess some patterns could be useful, but I still have to learn :(
/**
 * Interface with Google Reader Service, provides raw data (json, xml)
 */
public final class GoogleReaderDataProvider implements AuthenticationManager<GoogleCredentials> {
	private static final Logger LOG =
			Logger.getLogger(GoogleReaderDataProvider.class.getName());
	private HttpManager httpManager;
	private GoogleCredentials credentials = null;
	

	protected GoogleReaderDataProvider(ProxyCredentials proxyCredentials) {
		LOG.trace("Initializing Google Reader API");
		httpManager = new SimpleHttpManager(proxyCredentials);
/*		if (proxyCredentials == null) {
		} else {
			httpManager = new ProxyHttpManager(proxyCredentials);
		}*/
	}

//	protected GoogleReaderDataProvider(GoogleCredentials credentials) {
//		this(credentials, null);
//	}
	
	protected GoogleReaderDataProvider(GoogleCredentials credentials, ProxyCredentials proxyCredentials) {
		this(proxyCredentials);
		setCredentials(credentials);
	}
	
	protected GoogleReaderDataProvider(String username, String password) {
		this(username, password, null);
	}
	
	protected GoogleReaderDataProvider(String username, String password, ProxyCredentials proxyCredentials) {
		this(proxyCredentials);
		setCredentials(username, password);
		
	}

	/**
	 * Check if the user is authenticated
	 * 
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	private void checkIfAuthenticated() throws GoogleReaderException {
		if (hasCredentials()) {
			if (!credentials.hasAuthentication()) {
				throw new GoogleReaderException(
						"The user is not authenticated! Authentication is required to perform most API requests.");
			}
		} else {
			throw new GoogleReaderException("Credentials are not set!");
		}
	}



	
	public void clearCredentials() {
		LOG.trace("Clearing credentials");
		credentials = null;
		httpManager.clearCookies();
	}

	/**
	 * Get items from any feed. Useful method to get any feed as an Atom feed
	 * easily
	 * 
	 * @param feedUrl
	 *        The URL of the feed to get items from
	 * @return The feed items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getFeedItems(String feedUrl) throws GoogleReaderException {
		return getFeedItems(feedUrl, null);
	}

	/**
	 * Get items from any feed. Useful method to get any feed as an Atom feed
	 * easily
	 * 
	 * @param feedUrl
	 *        The URL of the feed to get items from
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return An XML String representing the Atom feed with the items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getFeedItems(String feedUrl, Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting feed items for feed: " + feedUrl);
		
		if (feedUrl == null) {
			throw new IllegalArgumentException("The feed url cannot be null!");
		}
		checkIfAuthenticated();
		
		String url = Constants.URL_FEED + urlEncode(feedUrl);
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(url, parameters, true);
	}

	/**
	 * Returns all the feeds with unread items as XML
	 * 
	 * @return The feeds with unread items (XML String)
	 * @throws GoogleReaderException
	 *         If the user is not autenticated
	 */
	public String getFeedsWithUnreadItems() throws GoogleReaderException {
		return getFeedsWithUnreadItems(null);
	}

	/**
	 * Returns all the feeds with unread items
	 * 
	 * @param outputFormat
	 *        The outputFormat (if null, defaults to XML)
	 * @return The feeds with unread items
	 * @throws GoogleReaderException
	 *         If the user is not autenticated
	 */
	public String getFeedsWithUnreadItems(OutputFormat outputFormat)
			throws GoogleReaderException {
		LOG.trace("Getting the list of feeds with unread items");
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("all", "true")); // Mandatory!
		if (outputFormat != null) {
			parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,
					outputFormat.getFormat()));
		}
		return httpManager.get(Constants.URL_UNREAD_COUNT, parameters, true);
	}

	/**
	 * Get an item's contents as JSON
	 * @param itemId The id of the item to retrieve
	 * @return The item's contents as JSON
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getItem(String itemId) throws GoogleReaderException {
		LOG.trace("Retrieving the contents of an item");
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		
		parameters.add(new Parameter(Constants.PARAMETER_ITEM_ID, itemId));
		parameters.add(new Parameter(Constants.PARAMETER_TOKEN, getToken()));
		return httpManager.post(Constants.URL_SEARCH_CONTENTS, parameters,
				true);
	}

	/**
	 * Get the reading list for an authenticated user (items from different
	 * feeds)
	 * 
	 * @return The reading list
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadingList() throws GoogleReaderException {
		return getReadingList(null);
	}

	/**
	 * Get the reading list for an authenticated user (items from different
	 * feeds)
	 * 
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return The reading list
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadingList(Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting reading list");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(Constants.URL_ITEMS_READING_LIST, parameters,
				true);
	}
	
	/**
	 * Get already read items (from any feed)
	 * 
	 * @return read items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadItems() throws GoogleReaderException {
		return getReadItems((String)null,null);
	}
	
	/**
	 * Get read items from a given feed as XML String representation (Atom feed)
	 * 
	 * @param feedDescriptor
	 *        The feed to get read items from
	 * @return read items from the specified feed as XML (atom)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadItems(FeedDescriptor feedDescriptor) throws GoogleReaderException {
		return getReadItems(feedDescriptor.getId(), null);
	}
	
	/**
	 * Get read items from a given feed as XML String representation (Atom feed)
	 * 
	 * @param feedDescriptor
	 *        The feed to get read items from
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return read items from the specified feed as XML (atom)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadItems(FeedDescriptor feedDescriptor, Integer numberOfElements) throws GoogleReaderException {
		return getReadItems(feedDescriptor.getId(), numberOfElements);
	} 	
	
	/**
	 * Get read items from a given feed as XML String representation (Atom feed)
	 * 
	 * @param feedId
	 *        The id of the feed to get read items from
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return read items from the specified feed as XML (atom)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadItems(String feedId, Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting read items from a feed");
		if (feedId == null) {
			throw new IllegalArgumentException(
					"The feed id to get read items from cannot be null!");
		}
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();

		String url = Constants.URL_FEED + urlEncode(feedId);
		List<Parameter> parameters = new ArrayList<Parameter>();
		
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		// exclude unread items from the results
		parameters.add(new Parameter(Constants.PARAMETER_STATE_FILTER,Constants.FILTER_CURRENT_USER_UNREAD));
		return httpManager.get(url, parameters, true);
	}
	
	/**
	 * Get already read items (from any feed)
	 * 
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return read items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadItems(Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting read items (from any feed)");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(Constants.URL_ITEMS_READ, parameters,
				true);
	}

	/**
	 * Get your shared items as an XML String (Atom)
	 * 
	 * @return your shared items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSharedItems() throws GoogleReaderException {
		return getSharedItems((Integer) null);
	}

	/**
	 * Get your shared items as an XML String (Atom)
	 * 
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return your shared items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSharedItems(Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting authenticated user shared items");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(Constants.URL_ITEMS_SHARED, parameters,
				true);
	}

	/**
	 * Get your shared items as an XML String (Atom)
	 * 
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return your shared items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSharedFriendsItems(Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting authenticated user shared items");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		parameters.add(new Parameter(Constants.PARAMETER_STATE_FILTER, "user/"+getUserId()+Constants.ITEM_STATE_READ));
		parameters.add(new Parameter("ck", ""+new Date().getTime()));
		parameters.add(new Parameter("client", "greader-unofficial"));

		return httpManager.get(Constants.URL_ITEMS_SHARED_FRIENDS, parameters,
				true);
	}

	/**
	 * Get items shared by another user (Atom). You need the user's Id number to
	 * use this, which is pretty easy to get through your shared items page
	 * 
	 * @param userId
	 *        The user id of the user to get shared items from
	 * @return items shared by another user
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSharedItems(String userId) throws GoogleReaderException {
		return getSharedItems(userId, null);
	}

	/**
	 * Get items shared by another user (Atom). You need the user's Id number to
	 * use this, which is pretty easy to get through your shared items page
	 * 
	 * @param userId
	 *        The user id of the user to get shared items from
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return items shared by another user
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSharedItems(String userId, Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting items shared by a user based on his id");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		if (userId == null) {
			throw new IllegalArgumentException("The user id cannot be null!");
		}
		checkIfAuthenticated();
		
		String url = Constants.URL_ITEMS_SHARED_BY_SOMEONE_ELSE.replace(Constants.URL_USER_TOKEN, urlEncode(""+userId));
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(url, parameters, false);
	}

	/**
	 * Get starred items (Atom)
	 * 
	 * @return starred items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getStarredItems() throws GoogleReaderException {
		return getStarredItems(null);
	}

	/**
	 * Get starred items (Atom)
	 * 
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return starred items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getStarredItems(Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting starred items");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(Constants.URL_ITEMS_STARRED, parameters,
				true);
	}

	/**
	 * Get subscriptions of the authenticated user (XML String)
	 * 
	 * @return The subscriptions
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSubscriptions()
			throws GoogleReaderException {
		return getSubscriptions(null);
	}
	
	/**
	 * Get subscriptions of the authenticated user
	 * 
	 * @param outputFormat The outputFormat (if null, defaults to XML)
	 * @return The subscriptions
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getSubscriptions(OutputFormat outputFormat) throws GoogleReaderException{
		LOG.trace("Getting subscriptions of the authenticated user");
		checkIfAuthenticated();

		List<Parameter> parameters = new ArrayList<Parameter>();
		if (outputFormat != null) {
			parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,
					outputFormat.getFormat()));
		}
		
		//to get as much information as possible
		parameters.add(new Parameter("complete", "true"));
		return httpManager.get(Constants.URL_SUBSCRIPTION_LIST, parameters,
				true);
		
	}

	/**
	 * A token is needed when marking elements as read, when editing
	 * subscriptions ...
	 * 
	 * @return A token that is valid for a few minutes
	 * @throws GoogleReaderException
	 *         If the user's not authenticated
	 */
	public String getToken() throws GoogleReaderException {
		LOG.trace("Getting a token");
		checkIfAuthenticated();
		return httpManager.get(Constants.URL_TOKEN, null, true);
	}

	/**
	 * Get unread items from a given feed as XML (atom)
	 * 
	 * @param feed
	 *        The feed to get unread items from
	 * @return unread items from the specified feed as XML (atom)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getUnreadItems(FeedDescriptor feed)
			throws GoogleReaderException {
		return getUnreadItems(feed, null);
	}
	public String getUnreadItems(FeedDescriptor feed, Integer numberOfElements)
	throws GoogleReaderException {
		if (feed == null) {
			throw new IllegalArgumentException(
					"The feed to get unread items from cannot be null!");
		}
		return getUnreadItems(feed.getId(), numberOfElements);
	}
	/**
	 * Get unread items from a given feed as XML String representation (Atom)
	 * 
	 * @param feed
	 *        The feed to get unread items from
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return unread items from the specified feed as XML (atom)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getUnreadItems(String feedId, Integer numberOfElements)
			throws GoogleReaderException {
		LOG.trace("Getting unread items from a feed");
		if (feedId == null) {
			throw new IllegalArgumentException(
					"The feed to get unread items from cannot be null!");
		}
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		
		// The feed id needs to be encoded
		String url = Constants.URL_FEED + urlEncode(feedId);
		List<Parameter> parameters = new ArrayList<Parameter>();
		
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		// exclude read items from the results
//		parameters.add(new Parameter(Constants.PARAMETER_STATE_FILTER,Constants.FILTER_CURRENT_USER_READ));
        String userId = getUserId ();
		parameters.add(new Parameter(Constants.PARAMETER_STATE_FILTER, "user/"+userId+Constants.ITEM_STATE_READ));
		parameters.add(new Parameter("ck", ""+new Date().getTime()));
		parameters.add(new Parameter("client", "greader-unofficial"));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.MONTH, -1);
		parameters.add(new Parameter("ot", ""+(calendar.getTimeInMillis()/1000)));
		return httpManager.get(url, parameters, true);
	}
	
	/**
	 * Encode special characters to be later used in a request
	 * @param s The string to encode
	 * @return The encoded string
	 * @throws GoogleReaderException If a problem occured while encoding the string
	 */
	private String urlEncode(String s) throws GoogleReaderException {
		try {
			s = URIUtil.encodeQuery(s);
			/**
			 * Problem with ? in feed url, we should encode it
			 * @author maratische
			 */
			s  = s.replaceAll("\\?", "%3F");
			return s;
		} catch (URIException e) {
			throw new GoogleReaderException(
					"Problem while encoding the feed id", e);
		}
	}
	

	

	public boolean hasCredentials() {
		return credentials != null;
	}


	public boolean login() throws AuthenticationException {
		LOG.trace("Trying to login");
		if (!hasCredentials()) {
			throw new AuthenticationException("Credentials are not set!");
		}
		
		if(isAuthenticated()) {
			LOG.trace("Already authenticated");
			httpManager.setAuth(getCredentials().getAuth());
			return true;
		}
		
		String result;
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(Constants.PARAMETER_LOGIN_USERNAME, credentials.getUsername()));
		parameters.add(new Parameter(Constants.PARAMETER_LOGIN_PASSWORD, credentials.getPassword()));
		parameters.add(new Parameter("accountType", "HOSTED_OR_GOOGLE"));//TODO move to Constants
		parameters.add(new Parameter("service", "reader"));
		parameters.add(new Parameter("source", "greader-unofficial-0.1-SNAPSHOT"));
		// parameters.add(new Parameter("service","reader")); // NOT mandatory	
		
		
		try {
			result =
					httpManager.post(Constants.URL_LOGIN,
							parameters, true);
			// LOG.debug("Login result: "+result);
			// Example:
			// SID=BLABLABLABLABLAVERYLONGID
			// LSID=BLABLABLABLAVERYLONGABITDIFFERENTID
			// Parse the result
			String sid =
					result.substring(result.indexOf(Constants.SID_TAG)
							+ Constants.SID_TAG.length(), result.indexOf('\n'));
			// LOG.debug("SID: "+sid);
			result = result.substring(result.indexOf('\n')+1);
			String lSid =
					result.substring(result.indexOf(Constants.LSID_TAG)
							+ Constants.LSID_TAG.length(), result.indexOf('\n'));
			result = result.substring(result.indexOf('\n')+1);
			String auth =
				result.substring(result.indexOf(Constants.AUTH_TAG)
						+ Constants.AUTH_TAG.length(), result.indexOf('\n'));
			// LOG.debug("LSID: "+lSid);
			httpManager.clearCookies(); // just to be sure...
			/*httpManager.addCookie(new Cookie(Constants.COOKIE_DOMAIN, Constants.SID, sid,
							Constants.COOKIE_PATH, Constants.COOKIE_MAX_AGE,
							Constants.COOKIE_SECURE));*/
			credentials.setSid(sid);
			credentials.setLSid(lSid);
			credentials.setAuth(auth);
			httpManager.setAuth(auth);
			return true;
		} catch (GoogleReaderException e) {
			throw new AuthenticationException(
					"Authentication failed using the provided credentials (they may be wrong or there was connectivity problem)",e);
		}
	}

	

	public boolean isAuthenticated() {
		if (hasCredentials()) {
			return credentials.hasAuthentication();
		}
		return false;
	}

	public void logout() {
		if(isAuthenticated()) {
			credentials.clearAuthentication();
			httpManager.clearCookies();
		}
	}

	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm) throws GoogleReaderException {
		return search(searchTerm, null, null, null);
	}

	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param feed
	 *        The feed in which to look for the search term
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, FeedDescriptor feed)
			throws GoogleReaderException {
		return search(searchTerm, feed, null, null);
	}

	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param feed
	 *        The feed in which to look for the search term
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, FeedDescriptor feed,
			Integer numberOfElements) throws GoogleReaderException {
		return search(searchTerm, feed, numberOfElements, null);
	}

	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param feed
	 *        The feed in which to look for the search term
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @param outputFormat
	 *        The outputFormat (if null, defaults to XML)
	 * @return The id's of the found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, FeedDescriptor feed,
			Integer numberOfElements, OutputFormat outputFormat)
			throws GoogleReaderException {
		LOG.trace("Searching for items");
		if (searchTerm == null) {
			throw new IllegalArgumentException(
					"The search term cannot be null!");
		}
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
		// The search term needs to be encoded
		parameters.add(new Parameter(Constants.PARAMETER_SEARCH_TERM, urlEncode(searchTerm)));
		
		if (feed != null) {
			// The feed id needs to be encoded
			parameters.add(new Parameter(
						Constants.PARAMETER_SEARCH_SOURCE_FEED, urlEncode(feed.getId())));
		}
		if (numberOfElements != null) {
			parameters.add(new Parameter(
					Constants.PARAMETER_SEARCH_NUMBER_OF_ELEMENTS,
					numberOfElements));
		}
		if (outputFormat != null) {
			parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,
					outputFormat.getFormat()));
		}
		return httpManager.get(Constants.URL_SEARCH_IDS, parameters,
				true);
	}

	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param feed
	 *        The feed in which to look for the search term
	 * @param outputFormat
	 *        The outputFormat (if null, defaults to XML)
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, FeedDescriptor feed,
			OutputFormat outputFormat) throws GoogleReaderException {
		return search(searchTerm, feed, null, outputFormat);
	}
	
	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, Integer numberOfElements)
			throws GoogleReaderException {
		return search(searchTerm, null, numberOfElements, null);
	}
	
	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @param outputFormat
	 *        The outputFormat (if null, defaults to XML)
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, Integer numberOfElements,
			OutputFormat outputFormat) throws GoogleReaderException {
		return search(searchTerm, null, numberOfElements, outputFormat);
	}
	
	/**
	 * Search for items based on a search term
	 * 
	 * @param searchTerm
	 *        The term to search for
	 * @param outputFormat
	 *        The outputFormat (if null, defaults to XML)
	 * @return The found items
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String search(String searchTerm, OutputFormat outputFormat)
			throws GoogleReaderException {
		return search(searchTerm, null, null, outputFormat);
	}
	
	
	public void setCredentials(GoogleCredentials credentials) {
		clearCredentials();
		LOG.trace("Setting credentials");
		this.credentials = credentials;
	}
	
	/**
	 * Removes any previously set credentials / cookie and sets the new
	 * credentials to use (login is NOT automatic)
	 * 
	 * @param username
	 *        The username or email
	 * @param password
	 *        The password
	 * @param login
	 *        True if authentication should occur after setting the new
	 *        credentials
	 */
	public void setCredentials(String username, String password) {
		this.setCredentials(new GoogleCredentials(username, password));
	}

	public GoogleCredentials getCredentials() {
		return credentials;
	}
	
	/**
	 * Get your current labels (tags)
	 * @return The list of subscriptions
	 * @throws GoogleReaderException
	 */
	public String getLabels() throws GoogleReaderException{
		return getLabels(null);
	}
	
	/**
	 * Get your current labels (tags)
	 * @param outputFormat The outputFormat (if null, defaults to XML)
	 * @return The list of labels
	 */
	public String getLabels(OutputFormat outputFormat)
			throws GoogleReaderException {
		LOG.trace("Getting labels list");
		
		checkIfAuthenticated();
		
		List<Parameter> parameters = new ArrayList<Parameter>();

		if (outputFormat != null) {
			parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,
					outputFormat.getFormat()));
		}

		return httpManager.get(Constants.URL_LABELS, parameters, true);
	}
	
	/**
	 * Get the reading list for an authenticated user and a specific label
	 * @parem label The label to get the reading list for
	 * @return The reading list (Atom as an XML String)
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getReadingListByLabel(String label) throws GoogleReaderException{
		return getReadingListByLabel(label, null);
	}
	
	/**
	 * Get the reading list for an authenticated user and a specific label
	 * 
	 * @parem label The label to get the reading list for (not the id but the NAME)
	 * @param numberOfElements
	 *        How many items should be retrieved (default if null: 20)
	 * @return The reading list (Atom as an XML String)
	 * @throws GoogleReaderException
	 *         If the user is not authenticated
	 */
	public String getReadingListByLabel(String label, Integer numberOfElements) throws GoogleReaderException{
		LOG.trace("Getting reading list for a specific label");
		if (numberOfElements != null) {
			if (numberOfElements <= 0) {
				throw new IllegalArgumentException(
						"The number of elements must be > 0");
			}
		}
		
		if(label == null) {
			throw new IllegalArgumentException("The label cannot be null!");
		}
		
		checkIfAuthenticated();
		
		String url = Constants.URL_LABEL + urlEncode(label);
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		if (numberOfElements != null) {
			parameters.add(new Parameter(Constants.PARAMETER_NUMBER_OF_RESULTS,
					numberOfElements));
		}
		return httpManager.get(url, parameters, true);
	}
	
	/**
	 * Export your subscription list to OPML
	 * @return The OPML String
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String exportSubscriptionsToOPML() throws GoogleReaderException{
		LOG.trace("Exporting the subscriptions list in OPML format");
		checkIfAuthenticated();
		return httpManager.get(Constants.URL_OPML_EXPORT, null, true);
	}
	
	/**
	 * Get an unread item (also marks it as READ!)
	 * @param token The shuffle token found in the user preferences (@see UserPreferences class)
	 * @return The next unread item (HTML page!)
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getNextUnreadItem(String token) throws GoogleReaderException{
		LOG.trace("Getting next unread item");
		checkIfAuthenticated();
		
		List<Parameter> parameters = new ArrayList<Parameter>();

		parameters.add(new Parameter("go","nextauto"));
		parameters.add(new Parameter(Constants.PARAMETER_SHUFFLE_TOKEN, token));		
		
		return httpManager.get(Constants.URL_NEXT_UNREAD, parameters, true);
	}
	
	/**
	 * Get the user preferences (JSON)
	 * @return The user preferences as JSON
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getUserPreferences() throws GoogleReaderException{
		LOG.trace("Getting user preferences");
		
		checkIfAuthenticated();
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,OutputFormat.JSON.getFormat()));
		return httpManager.get(Constants.URL_PREFERENCE_LIST, parameters, true);
	}	
	
	/**
	 * Get the user information (JSON)
	 * @return The user information as JSON
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getUserInformation() throws GoogleReaderException{
		LOG.trace("Getting user information");
		
		checkIfAuthenticated();
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("Authorization", "GoogleLogin auth="+credentials.getAuth()));
		//parameters.add(new Parameter(Constants.PARAMETER_OUTPUT_FORMAT,OutputFormat.JSON.getFormat()));
		return httpManager.get(Constants.URL_USER_INFO, parameters, true);
	}
	
	/**
	 * Mark all the items from a feed as read
	 * @param feedDescriptor The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markFeedAsRead(FeedDescriptor feed) throws GoogleReaderException {
		if(feed == null) {
			throw new IllegalArgumentException("The feed cannot be null!");
		}
		markFeedAsRead(feed.getId());
	}
	
	/**
	 * Mark all the items from a feed as read
	 * @param feedId The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markFeedAsRead(String feedId) throws GoogleReaderException {
		LOG.trace("Marking all items from a feed as read");
		
		if(feedId == null) {
			throw new IllegalArgumentException("The feed id cannot be null!");
		}
		
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
				
		parameters.add(new Parameter("s", urlEncode(feedId)));
		parameters.add(new Parameter(Constants.PARAMETER_TOKEN, getToken()));
		String result = httpManager.post(Constants.URL_MARK_ALL_AS_READ, parameters,true);
		
		if(!"OK".equals(result)) {
			throw new GoogleReaderException("The operation failed (no more details, sorry)");
		}
	}
	
	
	/**
	 * Mark the item from a feed as read
	 * @param itemId The Item to mark as read
	 * @param feedId The feed 
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsRead(ItemDescriptor item, FeedDescriptor feed) throws GoogleReaderException {
		if(item == null) {
			throw new IllegalArgumentException("The item cannot be null!");
		}
		if(feed == null) {
			throw new IllegalArgumentException("The feed cannot be null!");
		}
		markItemAsRead(item.getUri(), feed.getId());
	}
	/**
	 * Mark the item from a feed as read
	 * @param itemId The Item to mark as read
	 * @param feedId The feed 
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsRead(String itemId, String feedId) throws GoogleReaderException {
		LOG.trace("Marking item from a feed as read");
		markItemAs(itemId, feedId, "a", Constants.ITEM_STATE_READ);
	}
	public void markItemAsStarred(String itemId, String feedId) throws GoogleReaderException {
		LOG.trace("Marking item from a feed as Starred");
		markItemAs(itemId, feedId, "a", Constants.ITEM_STATE_STARRED);
	}
	public void markItemAsUnStarred(String itemId, String feedId) throws GoogleReaderException {
		LOG.trace("Marking item from a feed as Starred");
		markItemAs(itemId, feedId, "r", Constants.ITEM_STATE_STARRED);
	}
	/**
	 * Mark the item from a feed as Starred
	 * this method is dublicaed from markItemAsRead - TODO I should think about it, optimize
	 * @param itemId The Item to mark as read
	 * @param feedId The feed 
	 * @param markValue - type of mark
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	private void markItemAs(String itemId, String feedId, String markParameter, String markValue) throws GoogleReaderException {
		
		if(itemId == null) {
			throw new IllegalArgumentException("The item id cannot be null!");
		}
		
		if(feedId == null) {
			throw new IllegalArgumentException("The feed id cannot be null!");
		}
		
		checkIfAuthenticated();
		List<Parameter> parameters = new ArrayList<Parameter>();
				
        String userId = getUserId ();
        if ( userId == null ) {
                throw new GoogleReaderException ( "Couldn't retrieve User Id " );
        }

		parameters.add(new Parameter("s", urlEncode(feedId)));
		parameters.add(new Parameter("i", urlEncode(itemId)));
		parameters.add(new Parameter(Constants.PARAMETER_TOKEN, getToken()));
		parameters.add(new Parameter("async", "true"));
		parameters.add(new Parameter("pos", "0"));
		parameters.add(new Parameter(markParameter, "user/"+userId+markValue));
		String result = httpManager.post(Constants.URL_MARK_ITEM_AS_READ, parameters,true);
		
		if(!"OK".equals(result)) {
			throw new GoogleReaderException("The operation failed (no more details, sorry)");
		}
	}
	
        /**
         * Mark all the items in all feeds in a label as read
         * @param label The Label String to mark as read
         * @throws GoogleReaderException If the user is not authenticated
         */
        public void markLabelAsRead ( String label ) throws GoogleReaderException {
//                LOG.trace ( "Marking all items from a feed as read" );
	
                if ( label == null ) {
                        throw new IllegalArgumentException ( "The lable name cannot be null!" );
                }

                checkIfAuthenticated ();
                List<Parameter> parameters = new ArrayList<Parameter> ();

                String userId = getUserId ();
                if ( userId == null ) {
                        throw new GoogleReaderException ( "Couldn't retrieve User Id " );
                }

                parameters.add ( new Parameter ( "s" , urlEncode ( "user/" + userId + "/label/" + label ) ) );
                parameters.add ( new Parameter ( Constants.PARAMETER_TOKEN , getToken () ) );
                String result = httpManager.post ( Constants.URL_MARK_ALL_AS_READ , parameters , true );

                if ( !"OK".equals ( result ) ) {
                        throw new GoogleReaderException ( "The operation failed (no more details, sorry)" );
                }
        }

	/**
	 * The feed you want to subscribe to
	 * (no error if you are already subscribed to the given feed)
	 * @param feed The feed to subscribe to
	 * @param title The title for the new subscription
	 * @throws GoogleReaderException If the user is not authenticated
	 * @throws IllegalArgumentException If the feed is null or if the title is null or if the title is empty
	 */
	public void addSubscription(FeedDescriptor feedDescriptor, String title) throws GoogleReaderException{
		if(feedDescriptor == null) {
			throw new IllegalArgumentException("The feed descriptor cannot be null!");
		}
		
		addSubscription(feedDescriptor.getId(), title);
	}
	
	/**
	 * The feed you want to unsubscribe from
	 * (no error if you are not subscribed to the given feed)
	 * @param feed The feed to unsubscribe from
	 * @throws GoogleReaderException If the user is not authenticated
	 * @throws IllegalArgumentException If the feed is null
	 */
	public void removeSubscription(FeedDescriptor feedDescriptor) throws GoogleReaderException{
		if(feedDescriptor == null) {
			throw new IllegalArgumentException("The feed descriptor cannot be null!");
		}
		removeSubscription(feedDescriptor.getId());
	}
	
	/**
	 * The url or feed id (@see FeedDescriptor class) of a feed you want to subscribe to
	 * (no error if you are already subscribed to the given feed)
	 * @param feedUrl The url or feed id (@see FeedDescriptor class) to subscribe to
	 * @param title The title for the new subscription
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void addSubscription(String feedUrl, String title) throws GoogleReaderException{
		editSubscription(feedUrl,title, true);
	}
	
	/**
	 * The url or feed id (@see FeedDescriptor class) of a feed you want to unsubscribe from
	 * (no error if you are not already subscribed to the given feed)
	 * @param feedUrl The url or feed id (@see FeedDescriptor class) to unsubscribe from
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void removeSubscription(String feedUrl) throws GoogleReaderException{
		editSubscription(feedUrl,null, false);
	}
	
	/**
	 * Edit a subscription (add or remove)
	 * @param feedUrl The url of the feed to subscribe to or to unsubscribe from
	 * @param title The title for the subscription to add (can be null if you remove a subscription)
	 * @param add If true, the subscription is to be added; if false, it will be removed
	 * @throws GoogleReaderException If the user is not authenticated
	 * @throws IllegalArgumentException If the feed is null or if you try to add a subscription but the title is null or empty
	 */
	private void editSubscription(String feedUrl, String title, boolean add) throws GoogleReaderException{
		LOG.trace("Editing a subscription (adding or removing one");
		
		if(feedUrl == null) {
			throw new IllegalArgumentException("The feed url cannot be null!");
		}
		if(add && (title == null || "".equals(title))) {
			throw new IllegalArgumentException("The title cannot be null!");
		}
		
		checkIfAuthenticated();
		
		// If we received a normal url and not the feedId from a FeedDescriptor, no problem
		if(!feedUrl.startsWith("feed/")) {
			feedUrl = "feed/"+feedUrl;
		}
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		
		if(add) {
			parameters.add(new Parameter(Constants.EDIT_SUBSCRIPTION_ACTION, Constants.EDIT_SUBSCRIPTION_SUBSCRIBE));
		}else {
			parameters.add(new Parameter(Constants.EDIT_SUBSCRIPTION_ACTION, Constants.EDIT_SUBSCRIPTION_UNSUBSCRIBE));
		}
		parameters.add(new Parameter(Constants.EDIT_SUBSCRIPTION_FEED, urlEncode(feedUrl)));
		if(add) {
			parameters.add(new Parameter("t",urlEncode(title)));
		}
		parameters.add(new Parameter(Constants.PARAMETER_TOKEN, getToken()));
		String result = httpManager.post(Constants.URL_EDIT_SUSCRIPTION, parameters,true);
		
		if(!"OK".equals(result)) {
			throw new GoogleReaderException("The operation failed (no more details, sorry)");
		}
	}
	
        /**
         * Edit a subscription Label ( add/remove to label )
         * @param feedUrl The url of the feed to subscribe to or to unsubscribe from
         * @param title The title for the subscription to add (can be null if you remove a subscription)
         * @param label The label for the subscription to be added to
         * @param add If true, the subscription is to be added to label; if false, it will be removed
         * @throws GoogleReaderException If the user is not authenticated
         * @throws IllegalArgumentException If the feed is null or if you try to add a subscription but the title is null or empty
         */
        public void editSubscriptionLabel ( String feedUrl , String title , String label , boolean add ) throws GoogleReaderException {
//                LOG.trace ( "Editing a subscription (adding or removing one" );
	
                if ( feedUrl == null ) {
                        throw new IllegalArgumentException ( "The feed url cannot be null!" );
                }
                if ( add && ( label == null || "".equals ( label ) ) ) {
                        throw new IllegalArgumentException ( "The title cannot be null!" );
                }
	
                checkIfAuthenticated ();
	
                // If we received a normal url and not the feedId from a FeedDescriptor, no problem
                if ( !feedUrl.startsWith ( "feed/" ) ) {
                        feedUrl = "feed/" + feedUrl;
                }
	
                List<Parameter> parameters = new ArrayList<Parameter> ();
                String userId = getUserId ();
                if ( userId == null ) {
                        throw new GoogleReaderException ( "Couldn't retrieve User Id " );
}

                if ( add ) {
                        parameters.add ( new Parameter ( Constants.EDIT_SUBSCRIPTION_FEED_ADD , urlEncode ( "user/" + userId + "/label/" + label ) ) );
                } else {
                        parameters.add ( new Parameter ( Constants.EDIT_SUBSCRIPTION_FEED_REMOVE , urlEncode ( "user/" + userId + "/label/" + label ) ) );
                }
                parameters.add ( new Parameter ( Constants.EDIT_SUBSCRIPTION_FEED , urlEncode ( feedUrl ) ) );
                parameters.add ( new Parameter ( Constants.EDIT_SUBSCRIPTION_ACTION , Constants.EDIT_SUBSCRIPTION_EDIT_ACTION ) );
                if ( add ) {
                        parameters.add ( new Parameter ( "t" , urlEncode ( title ) ) );
                }
                parameters.add ( new Parameter ( Constants.PARAMETER_TOKEN , getToken () ) );
                String result = httpManager.post ( Constants.URL_EDIT_SUSCRIPTION , parameters , true );

                if ( !"OK".equals ( result ) ) {
                        throw new GoogleReaderException ( "The operation failed (no more details, sorry)" );
                }
        }

        /**
         * Gets User Id From Google Reader
         * @return String The Google user ID
         * @throws GoogleReaderException 
         */
        public String getUserId () throws GoogleReaderException {
                try {
                        JSONObject jSONObject = new JSONObject ( getUserInformation () );
                        String userId = jSONObject.getString ( "userId" );
                        return userId;
                } catch ( GoogleReaderException ex ) {
                        LOG.error ( "Google Reader Exception  while Getting User ID :  "+ex );
                        throw ex;
                } catch ( JSONException ex ) {
                        LOG.error ( "JSON Exception while Getting User ID : "+ex );
                }
                return null;
        }
}
