package be.lechtitseb.google.reader.api.model.authentication;

/**
 * Basic Credentials
 */
public abstract class BasicCredentials {
	private String username = "";
	private String password = "";

	public BasicCredentials(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	public void clearCredentials() {
		setUsername(null);
		setPassword(null);
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		if (password == null) {
			this.password = "";
		} else {
			this.password = password;
		}
	}

	public void setUsername(String username) {
		if (username == null) {
			this.username = "";
		} else {
			this.username = username;
		}
	}
}
