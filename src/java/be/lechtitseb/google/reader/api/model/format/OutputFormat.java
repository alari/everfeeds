package be.lechtitseb.google.reader.api.model.format;

/**
 * Output formats usable with some API requests
 */
public enum OutputFormat {
	XML("xml", "XML"),
	JSON("json","JSON");
	
	private OutputFormat(final String format, final String displayName) {
		this.format = format;
		this.displayName = displayName;
	}
	
	private String displayName;
	private String format;
	
	/**
	 * Get the displayable name for this output format
	 * @return The displayable name for this output format 
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Get the Google API output format usable in API requests
	 * @return The Google API output format usable in API requests
	 */
	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return format.toString();
	}
	
	
	
	
}
