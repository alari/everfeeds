package be.lechtitseb.google.reader.api.util;

import java.io.StringReader;

import org.jdom.Document;

import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

/**
 * Useful methods to manipulate Atom feeds
 */
public class AtomUtil {
	/**
	 * Read an Atom feed from its XML String representation
	 * @param xmlContent The xml content
	 * @return The ROME representation of this Atom feed
	 * @throws GoogleReaderException
	 */
	public static SyndFeed getAtomFeed(String xmlContent) throws GoogleReaderException {
		 try {
			return new SyndFeedInput().build(new StringReader(xmlContent));
		} catch (IllegalArgumentException e) {
			throw new GoogleReaderException("The provided xml content is not a valid Atom feed!",e);
		} catch (FeedException e) {
			throw new GoogleReaderException("The provided xml content is not a valid Atom feed!",e);
		}
	}
	
	/**
	 * Read an Atom feed from a JDOM Document
	 * @param xmlContent The JDOM document representing the Atom feed
	 * @return The ROME feed representation of this Atom content
	 * @throws GoogleReaderException
	 */
	public static SyndFeed getAtomFeed(Document doc) throws GoogleReaderException {
		try {
			return new SyndFeedInput().build(doc);
		} catch (IllegalArgumentException e) {
			throw new GoogleReaderException("The provided xml content is not a valid Atom feed!",e);
		} catch (FeedException e) {
			throw new GoogleReaderException("The provided xml content is not a valid Atom feed!",e);
		}
	}
	
}
