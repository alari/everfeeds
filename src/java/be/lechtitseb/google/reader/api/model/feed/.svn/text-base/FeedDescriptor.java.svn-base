package be.lechtitseb.google.reader.api.model.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//there are different kind of id's:
//1) a label of the user (regroups multiple feeds): user/15487536670345822901/label/misc
//2) normal feed url: feed/http://www.fark.com/fark.rss

//TODO when creating this object, parse the id to get the feed url and add another field to the class for it

/**
 * Represents the information given by Google about a feed
 */
public class FeedDescriptor {
	private Integer unreadItems;
	private String id;
	private String title;
	private Date newestItemTimestamp;
	private List<Label> categories;

	public FeedDescriptor() {
		categories = new ArrayList<Label>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getUnreadItems() {
		return unreadItems;
	}

	public void setUnreadItems(Integer unreadItems) {
		this.unreadItems = unreadItems;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getNewestItemTimestamp() {
		return newestItemTimestamp;
	}

	public void setNewestItemTimestamp(Date newestItemTimestamp) {
		this.newestItemTimestamp = newestItemTimestamp;
	}

	@Override
	public String toString() {
		return id.toString();
	}

	public List<Label> getCategories() {
		return categories;
	}

	public void setCategories(List<Label> categories) {
		if (categories == null) {
			this.categories.clear();
		} else {
			this.categories = categories;
		}
	}
}
