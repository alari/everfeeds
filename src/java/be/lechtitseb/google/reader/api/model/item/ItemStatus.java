package be.lechtitseb.google.reader.api.model.item;

/**
 * Status of an Item. THis class can be used to filter retrieved items in API requests
 */
public enum ItemStatus {
	READ("read","Read"),
	UNREAD("unread","Unread"),
	STARRED("starred","Starred"),
	SHARED("broadcast","Shared");

	
	
//FIXME Statues also applies to feeds so mh...
//	read 	 A read item will have the state read
//	kept-unread 	Once you've clicked on "keep unread", an item will have the state kept-unread
//	fresh 	When a new item of one of your feeds arrive, it's labeled as fresh. When (need to find what remove fresh label), the fresh label disappear.
//	starred 	When your mark an item with a star, you set it's starred state
//	broadcast 	When your mark an item as being public, you set it's broadcast state
//	reading-list 	All you items are flagged with the reading-list state. To see all your items, just ask for items in the state reading-list
//	tracking-body-link-used 	Set if you ever clicked on a link in the description of the item.
//	tracking-emailed 	Set if you ever emailed the item to someone.
//	tracking-item-link-used 	Set if you ever clicked on a link in the description of the item.
//	tracking-kept-unread 	Set if you ever mark your read item as unread. 

	
	
	
	
	private ItemStatus(final String status, final String displayName) {
		this.status = status;
		this.displayName = displayName;
	}
	
	private String displayName;
	private String status;
	
	
	/**
	 * Get the displayable name for this status
	 * @return The displayable name for this status 
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Get the item status usable in API requests
	 * @return The item status usable in API requests
	 */
	public String getStatus() {
		return status;
	}
	
	
}
