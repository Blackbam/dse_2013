package domain;

public class Doctor extends Person {

	private String title;

	public Doctor() {}

	public Doctor(String title, String firstName, String lastName) {
		super(firstName, lastName);
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Doctor [id = " + getId() + "title = " + getTitle() + ", firstName = " + getFirstName()
				+ ", lastName = " + getLastName() + "]";
	}

}
