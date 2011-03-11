
package be.lechtitseb.google.reader.api.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager;
import be.lechtitseb.google.reader.api.model.authentication.GoogleCredentials;
import be.lechtitseb.google.reader.api.model.authentication.ProxyCredentials;
import be.lechtitseb.google.reader.api.model.exception.AuthenticationException;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import be.lechtitseb.google.reader.api.model.feed.FeedDescriptor;
import be.lechtitseb.google.reader.api.model.feed.ItemDescriptor;
import be.lechtitseb.google.reader.api.model.feed.Label;
import be.lechtitseb.google.reader.api.model.format.OutputFormat;
import be.lechtitseb.google.reader.api.model.item.Item;
import be.lechtitseb.google.reader.api.model.preference.UserPreferences;
import be.lechtitseb.google.reader.api.model.user.UserInformation;
import be.lechtitseb.google.reader.api.util.GoogleReaderUtil;

//FIXME the methods javadoc are all copy/pasted from those in GoogleReaderDataProvider, I don't know how to fix this (and if it needs to or not)
//the problem is that I don't think I can use an interface (return types are different)
/**
 * Unofficial Google Reader API, can be used to manipulate a Google Reader
 * account. Once the user is authenticated, it is possible to retrieve his
 * feeds, get unread/starred/... items, add new subscriptions, create new tags
 * for feeds, ...
 */
public final class GoogleReader implements
		AuthenticationManager<GoogleCredentials> {
	private static final Logger LOG =
			Logger.getLogger(GoogleReader.class.getName());
	private GoogleReaderDataProvider api;

	public GoogleReader() {
		api = new GoogleReaderDataProvider(null);
	}

	public GoogleReader(String username, String password) {
		this(username, password, null);
	}

	public GoogleReader(String username, String password, ProxyCredentials proxyCredentials) {
		api = new GoogleReaderDataProvider(username, password, proxyCredentials);
	}

	public GoogleReader(GoogleCredentials credentials) {
		this(credentials, null);
	}

	public GoogleReader(GoogleCredentials credentials, ProxyCredentials proxyCredentials) {
		api = new GoogleReaderDataProvider(credentials, proxyCredentials);
	}

	public void clearCredentials() {
		api.clearCredentials();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#getCredentials()
	 */
	public GoogleCredentials getCredentials() {
		return api.getCredentials();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#hasCredentials()
	 */
	public boolean hasCredentials() {
		return api.hasCredentials();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#isAuthenticated()
	 */
	public boolean isAuthenticated() {
		return api.isAuthenticated();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#login()
	 */
	public boolean login() throws AuthenticationException {
		return api.login();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#logout()
	 */
	public void logout() {
		api.logout();
	}

	/* (non-Javadoc)
	 * @see be.lechtitseb.google.reader.api.model.authentication.AuthenticationManager#setCredentials(java.lang.Object)
	 */
	public void setCredentials(GoogleCredentials credentials) {
		api.setCredentials(credentials);
	}
	
	/**
	 * Search for a term in your subscriptions
	 * @param searchTerm The term to search for
	 * @return The list of found items
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public List<Item> search(String searchTerm) throws GoogleReaderException{
		return search(searchTerm,null,null);
	}
	
	/**
	 * Search for a term in your subscriptions
	 * @param searchTerm The term to search for
	 * @param numberOfElements How many items to retrieve
	 * @return The list of found items
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public List<Item> search(String searchTerm, Integer numberOfElements) throws GoogleReaderException{
		return search(searchTerm,null,numberOfElements);
	}
	
	/**
	 * Search for a term in your subscriptions
	 * @param searchTerm The term to search for
	 * @param feedDescriptor The feed in which to look for the search term
	 * @return The list of found items
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public List<Item> search(String searchTerm, FeedDescriptor feedDescriptor) throws GoogleReaderException{
		return search(searchTerm,feedDescriptor,null);
	}
	
	/**
	 * Search for a term in your subscriptions
	 * @param searchTerm The term to search for
	 * @param feedDescriptor The feed in which to look for the search term
	 * @param numberOfElements How many items to retrieve
	 * @return The list of found items
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public List<Item> search(String searchTerm, FeedDescriptor feedDescriptor, Integer numberOfElements) throws GoogleReaderException{
		List<Item> returnValue = new ArrayList<Item>();

		String searchResults = api.search(searchTerm,feedDescriptor, numberOfElements, OutputFormat.JSON);
		List<String> resultsIds =
				GoogleReaderUtil.getItemIdsFromJson(searchResults);
		for (String result : resultsIds) {
			String itemContent = api.getItem(result);
			returnValue.add(GoogleReaderUtil.getItemFromJson(itemContent));
		}
		return returnValue;
	}

	/**
	 * Get your current labels (tags)
	 * @return The list of labels
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public List<Label> getLabels() throws GoogleReaderException{
		String labelsContent = api.getLabels(OutputFormat.JSON);
		return GoogleReaderUtil.getLabelsFromJSON(labelsContent);
	}
	
	/**
	 * Export your subscription list to OPML
	 * @return The OPML String
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String exportSubscriptionsToOPML() throws GoogleReaderException{
		return api.exportSubscriptionsToOPML();
	}
	
	/**
	 * Get the user preferences
	 * @return The user preferences
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public UserPreferences getUserPreferences() throws GoogleReaderException{
		String raw = api.getUserPreferences();
		return GoogleReaderUtil.getUserPreferencesFromJson(raw);
	}
	
	/**
	 * Get the user information
	 * @return The user information
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public UserInformation getUserInformation() throws GoogleReaderException{
		String raw = api.getUserInformation();
		return GoogleReaderUtil.getUserInformationFromJson(raw);
	}
	
	/**
	 * Get an unread item (also marks it as READ!)
	 * @return The next unread item (HTML page!)
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getNextUnreadItem() throws GoogleReaderException{
		return api.getNextUnreadItem(this.getUserPreferences().getShuffleToken());
	}
	
	/**
	 * Get an unread item (also marks it as READ!)
	 * @param token The shuffle token found in the user preferences (@see UserPreferences class)
	 * @return The next unread item (HTML page!)
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public String getNextUnreadItem(String shuffleToken) throws GoogleReaderException{
		return api.getNextUnreadItem(shuffleToken);
	}
	
	/**
	 * Mark all the items from a feed as read
	 * @param feedId The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markFeedAsRead(String feedId) throws GoogleReaderException{
		api.markFeedAsRead(feedId);
	}
	
	/**
	 * Mark all the items from a feed as read
	 * @param feed The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markFeedAsRead(FeedDescriptor feed) throws GoogleReaderException{
		api.markFeedAsRead(feed);
	}
	
	public String getStarredItems(Integer numberOfElements) throws GoogleReaderException {
		return api.getStarredItems(numberOfElements);
	}
	
	public String getSharedItems(Integer numberOfElements) throws GoogleReaderException {
		return api.getSharedItems(numberOfElements);
	}
	
	public String getSharedItems() throws GoogleReaderException {
		return api.getSharedItems();
	}
	
	public String getSharedFriendsItems(Integer numberOfElements) throws GoogleReaderException {
		return api.getSharedFriendsItems(numberOfElements);
	}
	
	/**
	 * Mark the item from a feed as read
	 * @param feed The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsRead(ItemDescriptor item,FeedDescriptor feed) throws GoogleReaderException{
		api.markItemAsRead(item,feed);
	}
	
	/**
	 * @author maratische
	 * Mark the item from a feed as read
	 * @param feed The feed to mark as read
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsRead(String itemUrl,String feedId) throws GoogleReaderException{
		api.markItemAsRead(itemUrl,feedId);
	}
	
	/**
	 * @author maratische
	 * Mark the item from a feed as Starred
	 * @param feed The feed to mark as starred
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsStarred(String itemUrl,String feedId) throws GoogleReaderException{
		api.markItemAsStarred(itemUrl,feedId);
	}
	
	/**
	 * @author maratische
	 * Mark the item from a feed as Starred
	 * @param feed The feed to mark as starred
	 * @throws GoogleReaderException If the user is not authenticated
	 */
	public void markItemAsUnStarred(String itemUrl,String feedId) throws GoogleReaderException{
		api.markItemAsUnStarred(itemUrl,feedId);
	}
	
        public GoogleReaderDataProvider getApi () {
                return api;
        }
	//public void addSubscription()
}