package be.lechtitseb.google.reader.api.model.authentication;

/**
 * 
 * @author Marat Gizatullin
 *
 */
public class ProxyCredentials {

	private String host;
	private Integer port;
	private String login;
	private String password;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
