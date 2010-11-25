package be.lechtitseb.google.reader.api.model.user;

/**
 * Information about the Google Reader user
 */
public class UserInformation {
	/**
	 * Google User name
	 */
	private String username = "";
	/**
	 * The id of the user
	 */
	private String userId = "";
	/**
	 * The profile id of the user
	 */
	private String userProfileId = "";
	/**
	 * The user email
	 */
	private String email = "";
	/**
	 * true if the user has a blogger blog
	 */
	private boolean bloggerUser;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username == null) {
			this.username = "";
		}else {
		this.username = username;
		}
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		if(userId == null) {
			this.userId = "";
		}else {
		this.userId = userId;
		}
	}
	public String getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(String userProfileId) {
		if(userProfileId == null) {
			this.userProfileId = "";
		}else {
		this.userProfileId = userProfileId;
		}
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(email == null) {
			this.email = "";
		}else {
		this.email = email;
		}
	}
	public boolean isBloggerUser() {
		return bloggerUser;
	}
	public void setBloggerUser(boolean bloggerUser) {
		this.bloggerUser = bloggerUser;
	}
	
	
}
