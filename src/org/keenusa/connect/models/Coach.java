package org.keenusa.connect.models;

import org.joda.time.DateTime;

public class Coach extends ContactPerson {

	private long remoteId;
	private int age;
	private DateTime dateOfbirth;
	// status mostly empty in the source
	// inactive y/n can be used but there are only 2 out of 650 coaches marked as inactive
	private boolean isActive;
	private Location location;

	public Coach() {
		super();
	}

	public Coach(long remoteId, Gender gender, DateTime dateOfbirth, boolean isActive, Location location, String firstName, String middleName,
			String lastName, String email, String phone, String cellPhone) {
		super(firstName, middleName, lastName, email, phone, cellPhone, gender);
		this.remoteId = remoteId;
		this.dateOfbirth = dateOfbirth;
		this.isActive = isActive;
		this.location = location;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public DateTime getDateOfbirth() {
		return dateOfbirth;
	}

	public void setDateOfbirth(DateTime dateOfbirth) {
		this.dateOfbirth = dateOfbirth;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
