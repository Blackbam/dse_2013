package dse_domain.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;


public class Notification {

	@Id
	private String id;
	private User user;
	private String title;
	private String content;
	private Date date;
	private boolean hasBeenRead;
	
	public Notification(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.date = new Date();
		this.hasBeenRead = false;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user){
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

	public Date getDate() {
		return date;
	}
	
	public boolean hasBeenRead() {
		return hasBeenRead;
	}
	
	public void setHasBeenRead(boolean hasBeenRead) {
		this.hasBeenRead = hasBeenRead;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", user=" + user + ", title=" + title + ", content=" + content + ", date="
				+ date + ", hasBeenRead=" + hasBeenRead + "]";
	}
	
	

}
