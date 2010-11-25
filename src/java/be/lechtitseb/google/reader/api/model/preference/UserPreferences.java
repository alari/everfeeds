package be.lechtitseb.google.reader.api.model.preference;

/**
 * Google Reader preferences for a user
 */
public class UserPreferences {
	/**
	 * Token usable to get random unread item from Google Reader
	 */
	private String shuffleToken = "";
	/**
	 * The Google Reader start page
	 */
	private String startPage = "";
	/**
	 * The display language for the user
	 */
	private String displayLanguage = "";

	public String getDisplayLanguage() {
		return displayLanguage;
	}
	
	/**
	 * Do the user want to confirm when marking (a feed) as read?
	 */
	private boolean confirmMarkAsRead;

	public void setDisplayLanguage(String displayLanguage) {
		if (displayLanguage == null) {
			this.displayLanguage = "";
		} else {
			this.displayLanguage = displayLanguage;
		}
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		if (startPage == null) {
			this.startPage = "";
		} else {
			this.startPage = startPage;
		}
	}

	public String getShuffleToken() {
		return shuffleToken;
	}

	public void setShuffleToken(String shuffleToken) {
		if (shuffleToken == null) {
			this.shuffleToken = "";
		} else {
			this.shuffleToken = shuffleToken;
		}
	}
	
	public boolean isConfirmMarkAsRead() {
		return confirmMarkAsRead;
	}

	public void setConfirmMarkAsRead(boolean confirmMarkAsRead) {
		this.confirmMarkAsRead = confirmMarkAsRead;
	}
}


//FIXME there are more preferences, here's a complete example:

//{"prefs": [
//           {
//              "id": "animations-disabled",
//              "value": "false"
//           },
//           {
//              "id": "confirm-mark-as-read",
//              "value": "true"
//           },
//           {
//              "id": "shuffle-token",
//              "value": "-1111111111111111111"
//           },
//           {
//              "id": "scroll-tracking-enabled",
//              "value": "true"
//           },
//           {
//              "id": "show-scour-help-go-on",
//              "value": "false"
//           },
//           {
//              "id": "show-scroll-help",
//              "value": "false"
//           },
//           {
//              "id": "display-lang",
//              "value": "en"
//           },
//           {
//              "id": "is-card-view",
//              "value": "false"
//           },
//           {
//              "id": "mobile-use-transcoder",
//              "value": null
//           },
//           {
//              "id": "start-page",
//              "value": "user/11111111111111111111/label/things that matter"
//           },
//           {
//              "id": "show-minimized-navigation",
//              "value": "false"
//           },
//           {
//              "id": "show-oldest-interrupt",
//              "value": "true"
//           },
//           {
//              "id": "queue-sorting",
//              "value": "date"
//           },
//           {
//              "id": "friends-opt-in",
//              "value": "true"
//           },
//           {
//              "id": "read-items-visible",
//              "value": "false"
//           },
//           {
//              "id": "is-in-scour-mode",
//              "value": "false"
//           },
//           {
//              "id": "shared-design",
//              "value": null
//           },
//           {
//              "id": "show-min-navigation-help",
//              "value": "false"
//           },
//           {
//              "id": "design",
//              "value": "scroll"
//           },
//           {
//              "id": "mobile-num-entries",
//              "value": "9"
//           },
//           {
//              "id": "show-scour-help-go-off",
//              "value": "false"
//           },
//           {
//              "id": "show-all-tree-items",
//              "value": "false"
//           }
//        ]}
