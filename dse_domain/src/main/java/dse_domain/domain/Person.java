package dse_domain.domain;


public abstract class Person extends User{

	private String firstName;

	private String lastName;

	public Person() {
	}

	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Person [id = " + getId() + ", firstName = " + getFirstName() + ", lastName = " + getLastName() + "]";
	}

}
