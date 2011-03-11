package be.lechtitseb.google.reader.api.model.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.lechtitseb.google.reader.api.model.feed.FeedDescriptor;

/**
 * Represent an Item from a feed
 */
public class Item {
	private String contentType;
	private String website;
	private FeedDescriptor feedDescriptor;
	private String content;
	private String contentTextDirection;
	private String url;
	private String id;
	private String numericId;
	private String author;
	private String title;
	private Date updatedOn;
	private Date publishedOn;
	private Date crawledAt;
	private String mediaGroupContentUrl; // FIXME I don't know what this is
	private List<String> categories = new ArrayList<String>();
	
	
	
	
	public List<String> getCategories() {
		return categories;
	}


	public void setCategories(List<String> categories) {
		if(categories == null) {
			this.categories.clear();
		}else {
			this.categories = categories;
		}
	}


	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getContentTextDirection() {
		return contentTextDirection;
	}


	public void setContentTextDirection(String contentTextDirection) {
		this.contentTextDirection = contentTextDirection;
	}


	public Item() {
		
	}
	
	
	public FeedDescriptor getFeedDescriptor() {
		return feedDescriptor;
	}


	public void setFeedDescriptor(FeedDescriptor feedDescriptor) {
		this.feedDescriptor = feedDescriptor;
	}


	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Date getPublishedOn() {
		return publishedOn;
	}


	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}


	public Date getCrawledAt() {
		return crawledAt;
	}


	public void setCrawledAt(Date crawledAt) {
		this.crawledAt = crawledAt;
	}


	public String getMediaGroupContentUrl() {
		return mediaGroupContentUrl;
	}


	public void setMediaGroupContentUrl(String mediaGroupContentUrl) {
		this.mediaGroupContentUrl = mediaGroupContentUrl;
	}


	public String getNumericId() {
		return numericId;
	}


	public void setNumericId(String numericId) {
		this.numericId = numericId;
	}


	
	
	
	
}
