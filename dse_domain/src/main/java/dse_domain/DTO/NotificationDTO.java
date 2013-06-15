package dse_domain.DTO;

import java.io.Serializable;

import dse_domain.domain.User;

/**
 * Serializable notification model.
 */
public class NotificationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	private String title;

	private String content;

	public NotificationDTO(User user, String title, String content) {
		super();
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "NotificationDTO [user=" + user + ", title=" + title + ", content=" + content + "]";
	}

}
