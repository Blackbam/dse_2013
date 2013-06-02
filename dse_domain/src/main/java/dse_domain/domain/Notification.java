package dse_domain.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;


public class Notification {

	@Id
	private String id;
	private Person person;
	private String title;
	private String content;
	private Date date;
	private boolean hasBeenRead;
	
	public Notification(Person person, String title, String content) {
		this.person = person;
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
	
	public Person getPerson() {
		return person;
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

}
