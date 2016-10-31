package io.pivotal.demo.domain;

public class Person {
	
	private String firstName;
	private String lastName;
	private String emailAddress;
	
	public Person()
	{		
	}
	
	public Person(String firstName, String lastName, String emailAddress)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + "]";
	}

}
