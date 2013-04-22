package tdl.client;

/**
 * Provides objects that contain information about the authentication status
 * of the user.
 */

public class UserInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean loggedIn;
	private String email;
	private String loginURL;
	private String logoutURL;
	
	public UserInfo () {
		loggedIn = false;
		email = "";
		loginURL = "";
		logoutURL = "";
	}
	
	public UserInfo(boolean loggedIn, String email, String loginURL,
			String logoutURL) {
		this.loggedIn = loggedIn;
		this.email = email;
		this.loginURL = loginURL;
		this.logoutURL = logoutURL;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getEmail() {
		return email;
	}

	public String getLoginURL() {
		return loginURL;
	}

	public String getLogoutURL() {
		return logoutURL;
	}
	
	
}
