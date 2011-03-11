package be.lechtitseb.google.reader.api.model.feed;

/**
 * Label (or Tag) that can be applied to Feeds and also to Items
 */
public class Label {
	/**
	 * The complete label
	 */
	private String id = "";
	/**
	 * The name of this label
	 */
	private String name = "";
	/**
	 * Whether the label is shared or not
	 */
	private boolean shared;
	
	public Label() {
	}

	public Label(String id, String label) {
		this();
		this.id = id;
		name = label;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	@Override
	public String toString() {
		//FIXME mhh maybe not good, name could be null?
		return name.toString();
	}
	
	
	
}
