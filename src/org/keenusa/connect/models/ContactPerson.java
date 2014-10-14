package org.keenusa.connect.models;

import java.io.Serializable;

public class ContactPerson implements Serializable {

	private long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String phone;
	private String cellPhone;
	// can be empty in the source but present in recent records
	private Gender gender;

	public enum Gender {
		MALE, FEMALE, UNKNOWN
	}

	public ContactPerson() {
	}

	public ContactPerson(String firstName, String middleName, String lastName, String email, String phone, String cellPhone, Gender gender) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.cellPhone = cellPhone;
		this.gender = gender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFirstName());
		if (getMiddleName() != null && !getMiddleName().isEmpty()) {
			sb.append(" ");
			sb.append(getMiddleName());
		}
		sb.append(" ");
		sb.append(getLastName());
		return sb.toString();
	}

}
