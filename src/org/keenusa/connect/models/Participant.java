package org.keenusa.connect.models;

import org.joda.time.DateTime;

public class Participant extends ContactPerson {

	private String name = "";

	public Participant() {
		super();
	}

	public Participant(long remoteId, Gender gender, DateTime dateOfBirth, boolean isActive, Location location, String firstName, String middleName,
			String lastName, String email, String phone, String cellPhone, String foreignLanguages, String skillsExperience) {
		super(firstName, middleName, lastName, email, phone, cellPhone, gender);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
