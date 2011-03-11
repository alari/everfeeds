package be.lechtitseb.google.reader.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import be.lechtitseb.google.reader.api.core.Constants;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import be.lechtitseb.google.reader.api.model.feed.FeedDescriptor;
import be.lechtitseb.google.reader.api.model.feed.ItemDescriptor;
import be.lechtitseb.google.reader.api.model.feed.Label;
import be.lechtitseb.google.reader.api.model.item.Item;
import be.lechtitseb.google.reader.api.model.opml.Outline;
import be.lechtitseb.google.reader.api.model.preference.UserPreferences;
import be.lechtitseb.google.reader.api.model.user.UserInformation;
import be.lechtitseb.google.reader.api.util.xml.XMLReader;

//FIXME don't use static anymore
//FIXME move those hardcoded properties to the constants (or directly to a configuration file)
//FIXME add methods for Atom --> POJOs
//FIXME In case of null parameters, the behavior is inconsistent, sometimes I return null whereas sometimes I throw an IllegalArgumentException, this should be modified
//FIXME implement getSubscriptionsFromXml
//FIXME implement getSubscriptionsFromJson
//FIXME implement getItemIdsFromXml
//FIXME implement getLabelsFromXml
/**
 * Permits to easily manipulate feeds (Atom) provided by the Google Reader API
 */
public final class GoogleReaderUtil {
	private static final Logger LOG =
			Logger.getLogger(GoogleReaderUtil.class.getName());

	// FIXME maybe XPath would be better here
	@SuppressWarnings("unchecked")
	private static FeedDescriptor getFeedDescriptor(Element e) {
		LOG.trace("Getting feed descriptor for an element");
		FeedDescriptor returnValue = new FeedDescriptor();
		// Get string properties
		for (Element o : (List<Element>) e.getChildren("string")) {
			if (o.getAttributeValue("name").equals("id")) {
				returnValue.setId(o.getValue());
			} else if (o.getAttributeValue("name").equals("title")) {
				returnValue.setTitle(o.getValue());
			} else if (o.getAttributeValue("name").equals("sortid")) {
				// we don't need it I think, it's internal to google
			} else {
				LOG.debug("Unknown string property: "
						+ o.getAttributeValue("name"));
			}
		}
		// Get the labels and other properties
		Label label = null;
		// FIXME extract methods to make the code cleaner
		for (Element o : (List<Element>) e.getChildren("list")) {
			if (o.getAttributeValue("name").equals("categories")) {
				for (Element category : (List<Element>) o.getChildren()) {
					label = new Label();
					for (Element it : (List<Element>) category
							.getChildren("string")) {
						if (it.getAttributeValue("name").equals("id")) {
							label.setId(it.getValue());
						} else if (it.getAttributeValue("name").equals("label")) {
							label.setName(it.getValue());
						} else {
							LOG.debug("Unknown category property: "
									+ it.getAttributeValue("name"));
						}
					}
					returnValue.getCategories().add(label);
				}
			} else {
				LOG.debug("Unknown list property: "
						+ o.getAttributeValue("name"));
			}
		}
		// Get number properties
		for (Element o : (List<Element>) e.getChildren("number")) {
			if (o.getAttributeValue("name").equals("count")) {
				returnValue.setUnreadItems(Integer.valueOf(o.getValue()));
			} else if (o.getAttributeValue("name").equals(
					"newestItemTimestampUsec")) {
				long timestamp = Long.parseLong(o.getValue()) / 1000;
				returnValue.setNewestItemTimestamp(new Date(timestamp));
			} else if (o.getAttributeValue("name").equals("firstitemmsec")) {
				// FIXME what to do for this one?
			} else {
				LOG.debug("Unknown number property: "
						+ o.getAttributeValue("name"));
			}
		}
		return returnValue;
	}

	/**
	 * @param xmlContent
	 *        The XML content provided by the Google Reader API that represents
	 *        a list of feeds
	 * @return The list of corresponding feed descriptors
	 * @throws GoogleReaderException
	 *         If a problem occurs while creating the list or parsing the XML
	 *         content
	 */
	@SuppressWarnings("unchecked")
	public static List<FeedDescriptor> getFeedDescriptorsFromXml(
			String xmlContent) throws GoogleReaderException {
		LOG.trace("Getting Feed Descriptors from XML");
		List<FeedDescriptor> returnValue = new ArrayList<FeedDescriptor>();
		if (xmlContent == null) {
			LOG.warn("The XML content parameter was null!");
			return returnValue;
		}
		try {
			Document xmlDocument = new XMLReader().read(xmlContent);
			if (xmlDocument.getRootElement().getChild("list") == null) {
				LOG
						.debug("The list element is not present as it should be in:\n"
								+ xmlContent);
			} else {
				for (Element e : (List<Element>) xmlDocument.getRootElement()
						.getChild("list").getChildren()) {
					returnValue.add(getFeedDescriptor(e));
				}
			}
			return returnValue;
		} catch (JDOMException e) {
			throw new GoogleReaderException(
					"Problem while parsing the feed descriptors list", e);
		} catch (IOException e) {
			throw new GoogleReaderException(
					"Problem while parsing the feed descriptors list", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Outline> parseOPMLSubscriptions(String xmlContent) throws GoogleReaderException{
		try {
			List<Outline> result = new ArrayList<Outline>();
			Document xmlDocument = new XMLReader().read(xmlContent);
			if ("opml".equals(xmlDocument.getRootElement().getName())) {
				Element body = xmlDocument.getRootElement().getChild("body");
				if (body != null) {
					List<Element> childs = body.getChildren("outline");
					if (childs != null) {
						for (Element element : childs) {
							Outline outline = parseOutline(element);
							List<Element> childs2 = null;
							if ( (childs2 = element.getChildren("outline")) != null) {
								for (Element element2 : childs2) {
									Outline outline2 = parseOutline(element2);
									outline.getChilds().add(outline2);
								}
							}
							result.add(outline);
						}
					}
				}
			}
			return result;
		} catch (JDOMException e) {
			throw new GoogleReaderException(
					"Problem while parsing the OPML Subscriptions", e);
		} catch (IOException e) {
			throw new GoogleReaderException(
					"Problem while parsing the OPML Subscriptions", e);
		}
	}

	private static Outline parseOutline(Element element) {
		Outline outline = new Outline();
		if (element.getAttribute("text") != null) {
			outline.setText(element.getAttribute("text").getValue());
		}
		if (element.getAttribute("title") != null) {
			outline.setTitle(element.getAttribute("title").getValue());
		}
		if (element.getAttribute("htmlUrl") != null) {
			outline.setHtmlUrl(element.getAttribute("htmlUrl").getValue());
		}
		if (element.getAttribute("xmlUrl") != null) {
			outline.setXmlUrl(element.getAttribute("xmlUrl").getValue());
		}
		return outline;
	}

	/**
	 * Get feed descriptors from a JSON list of feeds containing unread items
	 * 
	 * @param jsonContent
	 *        The JSON content
	 * @return The feed descriptors
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the JSON content
	 */
	public static List<FeedDescriptor> getFeedDescriptorsFromFeedsWithUnreadItemsJson(
			String jsonContent) throws GoogleReaderException {
		LOG
				.trace("Getting Feed Descriptors for feeds with unread items (JSON)");
		List<FeedDescriptor> returnValue = new ArrayList<FeedDescriptor>();
		if (jsonContent == null) {
			LOG.warn("The JSON content parameter was null!");
			return returnValue;
		}
		try {
			JSONObject json = new JSONObject(jsonContent);
			JSONArray array = json.getJSONArray("unreadcounts");
			FeedDescriptor current = null;
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				current = new FeedDescriptor();
				current.setId(o.getString("id"));
				current.setUnreadItems(o.getInt("count"));
				long timestamp = o.getLong("newestItemTimestampUsec") / 1000;
				current.setNewestItemTimestamp(new Date(timestamp));
				returnValue.add(current);
			}
			return returnValue;
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
	}

	/**
	 * Create an item from JSON content (search results contents)
	 * 
	 * @param content
	 *        The JSON content
	 * @return The created item
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the content
	 */
	// FIXME this code needs a LOT of cleaning up
	// FIXME the content is encoded (i.e., &amp; instead of &), should it be
	// cleaned here?
	public static Item getItemFromJson(String content)
			throws GoogleReaderException {
		LOG.trace("Getting Item from JSON");
		if (content == null) {
			throw new IllegalArgumentException("The content cannot be null!");
		}
		Item returnValue = new Item();
		try {
			// LOG.debug(content);
			JSONObject json = new JSONObject(content);
			// LOG.debug(json.toString(3));
			if (!json.isNull("alternate")) {
				JSONObject properties =
						json.getJSONArray("alternate").getJSONObject(0);
				// LOG.debug("Website content type: " +
				// properties.getString("type"));
				returnValue.setContentType(properties.getString("type"));
				// LOG.debug("Website href: "+ properties.getString("href"));
				returnValue.setWebsite(properties.getString("href"));
			}
			FeedDescriptor feedDescriptor = new FeedDescriptor();
			// LOG.debug("Feed id: " + json.getString("id"));
			feedDescriptor.setId(json.getString("id"));
			// LOG.debug("Feed title: " + json.getString("title"));
			feedDescriptor.setTitle(json.getString("title"));
			returnValue.setFeedDescriptor(feedDescriptor);
			JSONObject items = json.getJSONArray("items").getJSONObject(0);
			if (!items.isNull("summary")) {
				JSONObject itemsSummary = items.getJSONObject("summary");
				// LOG.debug("Item Content: "+
				// itemsSummary.getString("content"));
				returnValue.setContent(itemsSummary.getString("content"));
				// LOG.debug("Item Text direction:
				// "+itemsSummary.getString("direction"));
				returnValue.setContentTextDirection(itemsSummary
						.getString("direction"));
			}
			JSONObject itemsAlternate =
					items.getJSONArray("alternate").getJSONObject(0);
			// LOG.debug("Page Content type: "+
			// itemsAlternate.getString("type"));
			// LOG.debug("Page href: " + itemsAlternate.getString("href"));
			returnValue.setUrl(itemsAlternate.getString("href"));
			// LOG.debug("Item id: " + items.getString("id"));
			returnValue.setId(items.getString("id"));
			if (!items.isNull("author")) {
				// System.out.println("Item author: " +
				// items.getString("author"));
				returnValue.setAuthor(items.getString("author"));
			}
			// LOG.debug("Item title: " + items.getString("title"));
			returnValue.setTitle(items.getString("title"));
			// LOG.debug("Item updated on: " + items.getString("updated"));
			returnValue.setUpdatedOn(new Date(items.getLong("updated") * 1000));
			// JSONObject origin = items.getJSONObject("origin");
			// if (!origin.isNull("htmlUrl")) {
			// LOG.debug("Blog url: " + origin.getString("htmlUrl"));
			// }
			// LOG.debug("Feed title: " + origin.getString("title"));
			// feedDescriptor.setTitle(origin.getString("title"));
			// LOG.debug("Blog feed id: " + origin.getString("streamId"));
			if (!items.isNull("categories")) {
				JSONArray categories = items.getJSONArray("categories");
				for (int i = 0; i < categories.length(); i++) {
					// LOG.debug("Category: " + categories.getString(i));
					returnValue.getCategories().add(categories.getString(i));
				}
			}
			if (!items.isNull("annotations")) {
				JSONArray annotations = items.getJSONArray("annotations");
				for (int i = 0; i < annotations.length(); i++) {
					// LOG.debug("Annotation: "+ annotations.getString(i));
					// FIXME not managed yet (don't know what it's composed of)
				}
			}
			// LOG.debug("Item published on: "+new
			// Date(items.getLong("published")*1000));
			returnValue.setPublishedOn(new Date(
					items.getLong("published") * 1000));
			// LOG.debug("Item crawled at: " +
			// items.getString("crawlTimeMsec"));
			returnValue.setCrawledAt(new Date(items.getLong("crawlTimeMsec")));
			if (!items.isNull("mediaGroup")) {
				JSONObject mediaGroup = items.getJSONObject("mediaGroup");
				LOG.debug("Media group content url: "
						+ mediaGroup.getJSONArray("content").getJSONObject(0)
								.getString("url"));
				returnValue.setMediaGroupContentUrl(mediaGroup.getJSONArray(
						"content").getJSONObject(0).getString("url"));
			}
			// FIXME Again the update time?
			// if (!json.isNull("updated")) {
			// System.out.println("updated: " + json.getString("updated"));
			// }
			if (!json.isNull("self")) {
				JSONObject self = json.getJSONArray("self").getJSONObject(0);
				String requestString = self.getString("href");
				// LOG.debug("Request href: " + requestString);
				// FIXME if someone tells me this code isn't scary...
				String numericId =
						requestString.substring(requestString.indexOf("?i=")
								+ "?i=".length(), requestString.indexOf("&T="));
				// LOG.debug("Numeric id: "+numericId);
				returnValue.setNumericId(numericId);
			}
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
		return returnValue;
	}

	/**
	 * Get a list of item ids from the search results (JSON)
	 * 
	 * @param content
	 *        The search results (JSON)
	 * @return The list of item ids
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the JSON content
	 */
	public static List<String> getItemIdsFromJson(String content)
			throws GoogleReaderException {
		LOG.trace("Getting Item Ids from JSON");
		List<String> returnValue = new ArrayList<String>();
		if (content == null) {
			return returnValue;
		}
		try {
			JSONObject json = new JSONObject(content);
			// System.out.println(json.toString(3));
			if (!json.isNull("results")) {
				JSONArray results = json.getJSONArray("results");
				for (int i = 0; i < results.length(); i++) {
					returnValue.add(results.getJSONObject(i).getString("id"));
				}
			}
			return returnValue;
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
	}

	/**
	 * Get a list of labels from the JSON content returned by the API
	 * 
	 * @param content
	 *        The JSON content
	 * @parem includeSpecialLabels Should the results include special labels
	 *        (starred, shared, ...)
	 * @return The list of labels
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the JSON content
	 */
	public static List<Label> getLabelsFromJSON(String content)
			throws GoogleReaderException {
		LOG.trace("Getting labels from JSON");
		List<Label> returnValue = new ArrayList<Label>();
		if (content == null) {
			return returnValue;
		}
		try {
			JSONObject json = new JSONObject(content);
			// System.out.println(json.toString(3));
			if (!json.isNull("tags")) {
				JSONArray labels = json.getJSONArray("tags");
				Label tmp = null;
				String id = null;
				String shared = null;
				for (int i = 0; i < labels.length(); i++) {
					tmp = new Label();
					id = labels.getJSONObject(i).getString("id");
					if (!labels.getJSONObject(i).isNull("shared")) {
						shared = labels.getJSONObject(i).getString("shared");
					}
					tmp.setId(id);
					if (id.indexOf(Constants.ITEM_STATE) >= 0) {
						// FIXME maybe these should be treated differently
						// (state object?)
						tmp.setName(id.substring(id
								.indexOf(Constants.ITEM_STATE)
								+ Constants.ITEM_STATE.length() + 1));
					} else if (id.indexOf("/label/") > 0) {
						tmp.setName(id.substring(id.indexOf("/label/")
								+ "/label/".length()));
					} else {
						LOG.warn("Unknown label type: " + id);
					}
					if ("all".equals(shared)) {
						// FIXME shared could be something else I think, like
						// one/multiple friend(s), so this should be modified
						tmp.setShared(true);
					}
					returnValue.add(tmp);
				}
			}
			return returnValue;
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
	}

	/**
	 * Get preferences from the raw content (JSON)
	 * 
	 * @param content
	 *        The preferences (JSON)
	 * @return The preferences as a friendlier pojo
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the JSON content
	 */
	public static UserPreferences getUserPreferencesFromJson(String content)
			throws GoogleReaderException {
		LOG.trace("Getting user preferences from JSON");
		if (content == null) {
			throw new IllegalArgumentException("The content is null!");
		}
		UserPreferences returnValue = new UserPreferences();
		try {
			JSONObject json = new JSONObject(content);
			// System.out.println(json.toString(3));
			if (!json.isNull("prefs")) {
				JSONArray results = json.getJSONArray("prefs");
				for (int i = 0; i < results.length(); i++) {
					if (!results.isNull(i)) {
						JSONObject current = results.getJSONObject(i);
						// LOG.debug("current: "+current.getString("value"));
						if ("shuffle-token".equals(current.getString("id"))) {
							returnValue.setShuffleToken(current
									.getString("value"));
						} else if ("start-page".equals(current.getString("id"))) {
							returnValue
									.setStartPage(current.getString("value"));
						} else if ("confirm-mark-as-read".equals(current
								.getString("id"))) {
							if ("true".equals(current.getString("value"))) {
								returnValue.setConfirmMarkAsRead(true);
							}
						}
						// FIXME there are other preferences, unused for now,
						// @see UserPreferences class
					} else {
						LOG
								.warn("Null element(s) in the user preferences (should not happen)");
					}
				}
			}
			return returnValue;
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
	}

	/**
	 * Get user information from the raw content (JSON)
	 * 
	 * @param content
	 *        The user information (JSON)
	 * @return The user information as a friendlier pojo
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the JSON content
	 */
	public static UserInformation getUserInformationFromJson(String content)
			throws GoogleReaderException {
		LOG.trace("Getting user preferences from JSON");
		if (content == null) {
			throw new IllegalArgumentException("The content is null!");
		}
		UserInformation returnValue = new UserInformation();
		try {
			JSONObject json = new JSONObject(content);
			// System.out.println(json.toString(3));
			// LOG.debug("Is blogger user: "+json.getString("isBloggerUser"));
			if ("true".equals(json.getString("isBloggerUser"))) {
				returnValue.setBloggerUser(true);
			}
			// LOG.debug("userId: "+json.getString("userId"));
			returnValue.setUserId(json.getString("userId"));
			// LOG.debug("userProfileId: "+json.getString("userProfileId"));
			returnValue.setUserProfileId(json.getString("userProfileId"));
			// LOG.debug("userEmail: "+json.getString("userEmail"));
			returnValue.setEmail(json.getString("userEmail"));
			return returnValue;
		} catch (JSONException e) {
			throw new GoogleReaderException(
					"Problem while manipulating the JSON content", e);
		}
	}

	/**
	 * Get feed descriptors from a XML list of feeds containing unread items
	 * 
	 * @param xmlContent
	 *        The XML content
	 * @return The feed descriptors
	 * @throws GoogleReaderException
	 *         If a problem occurs while parsing the XML content
	 */
	@SuppressWarnings("unchecked")
	public static List<FeedDescriptor> getFeedDescriptorsFromFeedsWithUnreadItemsXml(
			String xmlContent) throws GoogleReaderException {
		LOG.trace("Getting Feed Descriptors for feeds with unread items (XML)");
		//LOG.debug(xmlContent);
		List<FeedDescriptor> returnValue = new ArrayList<FeedDescriptor>();
		if (xmlContent == null) {
			LOG.warn("The XML content parameter was null!");
			return returnValue;
		}
		try {
			Document xmlDocument = new XMLReader().read(xmlContent);
			if (xmlDocument.getRootElement().getChild("list") == null) {
				LOG
						.debug("The list element is not present as it should be in:\n"
								+ xmlContent);
				return returnValue;
			}
			FeedDescriptor descriptor = null;
			for (Element item : (List<Element>) xmlDocument.getRootElement()
					.getChild("list").getChildren()) {
				descriptor = new FeedDescriptor();
				for (Element child : (List<Element>) item.getChildren()) {
					if ("id".equals(child.getAttributeValue("name"))) {
						descriptor.setId(child.getValue());
					} else if ("count".equals(child.getAttributeValue("name"))) {
						descriptor.setUnreadItems(Integer.valueOf(child
								.getValue()));
					} else if ("newestItemTimestampUsec".equals(child
							.getAttributeValue("name"))) {
						long time = Long.parseLong(child.getValue()) / 1000;
						descriptor.setNewestItemTimestamp(new Date(time));
					}
				}
				returnValue.add(descriptor);
			}
			return returnValue;
		} catch (JDOMException e) {
			throw new GoogleReaderException(
					"Problem while parsing the feed descriptors list", e);
		} catch (IOException e) {
			throw new GoogleReaderException(
					"Problem while parsing the feed descriptors list", e);
		}
	}
	
	/**
	 * get Items Description from RSS list 
	 * @param xmlContent
	 * @return
	 * @throws GoogleReaderException
	 */
	public static List<ItemDescriptor> getItemDescriptorsFromItemsXml(String xmlContent) throws GoogleReaderException {
		List<ItemDescriptor> items = new ArrayList<ItemDescriptor>();
		SyndFeed syndFeed = AtomUtil.getAtomFeed(xmlContent);
		if (syndFeed != null) {
			for (Object object : syndFeed.getEntries()) {
				SyndEntry entry = (SyndEntry)object;
				if (entry != null) {
					ItemDescriptor item = new ItemDescriptor();
					item.setUri(entry.getUri());
					item.setLink(entry.getLink());
					item.setTitle(entry.getTitle());
					if (entry.getDescription() != null) {
						item.setDescription(entry.getDescription().getValue());
						item.setDescriptionType(entry.getDescription().getType());
					} else if (entry.getContents() != null && entry.getContents().size()>0) {
						for (Object objectContent : entry.getContents()) {
							SyndContent content = (SyndContent)objectContent;
							if (content != null) {
								if (item.getDescription() == null) {
									item.setDescription(content.getValue());
								} else {
									item.setDescription(item.getDescription()+content.getValue());
								}
							}
						}
					}
					item.setUpdatedDate(entry.getUpdatedDate());
					//this field contents link to feed
					if (entry.getSource() != null) {
						item.setSourceUri(entry.getSource().getUri());
					}
					item.setAuthor(entry.getAuthor());
					items.add(item);
				}
			}
		}
		return items;
	}
	
}
