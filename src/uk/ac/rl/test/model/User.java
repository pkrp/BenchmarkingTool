package uk.ac.rl.test.model;

public class User {

	private String login;
	private String password;
	private String authType;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String login, String password, String authType) {
		this.login = login;
		this.password = password;
		this.authType = authType;
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

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String loginToString() {
		switch (login) {
		case "fa":
			return "facility admin";
		case "is":
			return "instrument scientist";
		default:
			return login;
		}
	}

	public String getId() {
		return String.format("%s/%s", authType, login);
	}
}
