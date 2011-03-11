package be.lechtitseb.google.reader.api.model.authentication;


/**
 * Google specific credentials
 */
public class GoogleCredentials extends BasicCredentials {
	private String sid = "";
	private String lSid = ""; // Unused for now it seems
	private String auth = "";

	public GoogleCredentials(String username, String password) {
		super(username, password);
	}

	/**
	 * Clear authentication information (sid, lsid)
	 */
	public void clearAuthentication() {
		setSid(null);
		setLSid(null);
		setAuth(null);
	}
	
	@Override
	public void clearCredentials() {
		super.clearCredentials();
	}

	public String getLSid() {
		return lSid;
	}

	public String getSid() {
		return sid;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * Whether we have SID + LSID or not
	 */
	public boolean hasAuthentication() {
		return (!sid.equals("") && !lSid.equals("") && !auth.equals(""));
	}

	public void setLSid(String lSid) {
		if (lSid == null) {
			this.lSid = "";
		} else {
			this.lSid = lSid;
		}
	}

	public void setSid(String sid) {
		if (sid == null) {
			this.sid = "";
		} else {
			this.sid = sid;
		}
	}
}
