package be.lechtitseb.google.reader.api.model.opml;

import java.util.ArrayList;
import java.util.List;

/**
 * Outline Item for Subsription Menu
 * @author maratische
 *
 */
public class Outline {

	private String title;
	private String text;
	private String xmlUrl;
	private String htmlUrl;
	
	private List<Outline> childs = new ArrayList<Outline>();

	public List<Outline> getChilds() {
		return this.childs;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getXmlUrl() {
		return xmlUrl;
	}
	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	
}
