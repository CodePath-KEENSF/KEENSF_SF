package org.keenusa.connect.models;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.keenusa.connect.helpers.CivicoreDateStringParser;
import org.keenusa.connect.helpers.CivicoreGenderStringParser;
import org.keenusa.connect.helpers.CivicoreParentRelationshipStringParser;
import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.models.remote.RemoteAthlete;

public class Athlete extends ContactPerson {

	private long id;
	private long remoteId;
	private String nickName;
	private DateTime dateOfBirth;
	private String primaryLanguage;
	// status can be empty in the source. values observed : Approved and Inactive
	// if Approved or empty = ACTIVE
	private boolean isActive;
	private Location location;
	private Parent primaryParent;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;
	private int numberOfSessionsAttended;
	private DateTime dateLastAttended;

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
			athlete.setNickName(remoteAthlete.getNickName());
			athlete.setDateOfBirth(CivicoreDateStringParser.parseDate(remoteAthlete.getDob()));
			String status = remoteAthlete.getStatus();
			boolean isActiveValue = true;
			if (status != null && status.equalsIgnoreCase("inactive")) {
				isActiveValue = false;
			}
			athlete.setActive(isActiveValue);
			athlete.setPhone(remoteAthlete.getHomePhone());
			athlete.setEmail(remoteAthlete.getEmail());
			athlete.setPrimaryLanguage(remoteAthlete.getPrimaryLanguageAtHome());
			athlete.setGender(CivicoreGenderStringParser.parseGenderString(remoteAthlete.getGender()));

			Location location = new Location();
			location.setCity(remoteAthlete.getCity());
			location.setState(remoteAthlete.getState());
			location.setZipCode(remoteAthlete.getZipCode());
			athlete.setLocation(location);

			Parent parent = new Parent();
			parent.setFirstName(remoteAthlete.getPrimaryParentGuardianFirstName());
			parent.setLastName(remoteAthlete.getPrimaryParentGuardianLastName());
			parent.setCellPhone(remoteAthlete.getParentGuardianCellPhone());
			parent.setEmail(remoteAthlete.getParentGuardianEmailAddress());
			parent.setPhone(remoteAthlete.getParentGuardianHomePhone());
			parent.setParentRelationship(CivicoreParentRelationshipStringParser.parseParentRelationshipString(remoteAthlete
					.getParentGuardianRelationship()));
			parent.setPrimary(true);
			athlete.setPrimaryParent(parent);
			athlete.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAthlete.getCreated()).getMillis());
			athlete.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAthlete.getUpdated()).getMillis());
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
		return (new Period(dateOfBirth, (new DateTime()))).getYears();
	}

	public DateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(DateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public Parent getPrimaryParent() {
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
	}

	public long getRemoteCreateTimestamp() {
		return remoteCreateTimestamp;
	}

	public void setRemoteCreateTimestamp(long remoteCreateTimestamp) {
		this.remoteCreateTimestamp = remoteCreateTimestamp;
	}

	public long getRemoteUpdatedTimestamp() {
		return remoteUpdatedTimestamp;
	}

	public void setRemoteUpdatedTimestamp(long remoteUpdatedTimestamp) {
		this.remoteUpdatedTimestamp = remoteUpdatedTimestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumberOfSessionsAttended() {
		return numberOfSessionsAttended;
	}

	public void setNumberOfSessionsAttended(int numberOfSessionsAttended) {
		this.numberOfSessionsAttended = numberOfSessionsAttended;
	}

	public DateTime getDateLastAttended() {
		return dateLastAttended;
	}

	public void setDateLastAttended(DateTime dateLastAttended) {
		this.dateLastAttended = dateLastAttended;
	};

}
