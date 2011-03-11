package be.lechtitseb.google.reader.api.model.feed;

import java.util.Date;

/**
 * Item Description
 * parsed by ROMO
 * @author maratische
 *
 */
public class ItemDescriptor {
	private String uri;
	private String title;
	private String link;
	private String description;
	private String descriptionType;
	private Date updatedDate;
	private String sourceUri;
	private String author;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(this.getClass().getName()).append(":\n");
		sb.append("uri:").append(uri).append("\n");
		sb.append("link:").append(link).append("\n");
		sb.append("title:").append(title).append("\n");
		sb.append("description:").append(description).append("]\n");
		return sb.toString();
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionType() {
		return descriptionType;
	}
	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}

	public String getSourceUri() {
		return sourceUri;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
