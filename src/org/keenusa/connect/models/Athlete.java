package org.keenusa.connect.models;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.keenusa.connect.networking.RemoteAthlete;

public class Athlete extends ContactPerson {

	private long remoteId;
	// can be empty in the source but present in recent records
	private String nickName;
	// to be calculated from dob
	private int age;
	private DateTime dateOfBirth;
	private String primaryLanguage;
	// status can be empty in the source. values observed : Approved and Inactive
	// if Approved or empty = ACTIVE
	private boolean isActive;
	private Location location;
	private Parent primaryParent;

	public Athlete() {
		super();
	}

	public Athlete(long remoteId, String nickName, Gender gender, DateTime dateOfBirth, String primaryLanguage, boolean isActive, Location location,
			Parent primaryParent, String firstName, String middleName, String lastName, String email, String phone, String cellPhone) {
		super(firstName, middleName, lastName, email, phone, cellPhone, gender);
		this.remoteId = remoteId;
		this.nickName = nickName;
		this.dateOfBirth = dateOfBirth;
		this.primaryLanguage = primaryLanguage;
		this.isActive = isActive;
		this.location = location;
		this.primaryParent = primaryParent;
	}

	public static Athlete fromRemoteAthlete(RemoteAthlete remoteAthlete) {
		Athlete athlete = null;
		if (remoteAthlete != null) {
			athlete = new Athlete();
			athlete.setRemoteId(Long.valueOf(remoteAthlete.getRemoteId()));
			athlete.setFirstName(remoteAthlete.getFirstName());
			athlete.setLastName(remoteAthlete.getLastName());
		}
		return athlete;
	}

	public static List<Athlete> fromRemoteAthleteList(List<RemoteAthlete> remoteAthleteList) {
		List<Athlete> athletes = null;
		if (remoteAthleteList != null) {
			athletes = new ArrayList<Athlete>(remoteAthleteList.size());
			for (RemoteAthlete remoteAthlete : remoteAthleteList) {
				Athlete athlete = fromRemoteAthlete(remoteAthlete);
				athletes.add(athlete);
			}

		} else {
			athletes = new ArrayList<Athlete>();
		}
		return athletes;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getAge() {
		return age;
	}

	public DateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(DateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		this.age = new Period(dateOfBirth, (new DateTime())).getYears();
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ContactPerson getPrimaryParent() {
		return primaryParent;
	}

	public void setPrimaryParent(Parent primaryParent) {
		this.primaryParent = primaryParent;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	};

}
