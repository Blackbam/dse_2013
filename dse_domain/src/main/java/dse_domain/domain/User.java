package dse_domain.domain;

import org.springframework.data.annotation.Id;

/**
 * Abstract class containing common properties and methods for system users.
 */
public abstract class User {

	@Id
	private String id;

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
