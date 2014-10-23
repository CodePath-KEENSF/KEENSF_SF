package org.keenusa.connect.models;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.keenusa.connect.helpers.CivicoreDateStringParser;
import org.keenusa.connect.helpers.CivicoreGenderStringParser;
import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.models.remote.RemoteCoach;

public class Coach extends ContactPerson {

	private long remoteId;
	private DateTime dateOfBirth;
	// status mostly empty in the source
	// inactive y/n can be used but there are only 2 out of 650 coaches marked as inactive
	private boolean isActive;
	private Location location;
	private String foreignLanguages;
	private String skillsExperience;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;

	public Coach() {
		super();
	}

	public Coach(long remoteId, Gender gender, DateTime dateOfBirth, boolean isActive, Location location, String firstName, String middleName,
			String lastName, String email, String phone, String cellPhone, String foreignLanguages, String skillsExperience) {
		super(firstName, middleName, lastName, email, phone, cellPhone, gender);
		this.remoteId = remoteId;
		this.dateOfBirth = dateOfBirth;
		this.isActive = isActive;
		this.location = location;
		this.foreignLanguages = foreignLanguages;
		this.skillsExperience = skillsExperience;
	}

	public static Coach fromRemoteCoach(RemoteCoach remoteCoach) {
		Coach coach = null;
		if (remoteCoach != null) {
			coach = new Coach();
			coach.setRemoteId(Long.valueOf(remoteCoach.getRemoteId()));
			coach.setFirstName(remoteCoach.getFirstName());
			coach.setLastName(remoteCoach.getLastName());
			coach.setMiddleName(remoteCoach.getMiddleName());
			coach.setDateOfBirth(CivicoreDateStringParser.parseDate(remoteCoach.getDateOfbirth()));
			String inactiveString = remoteCoach.getInactive();
			boolean isActiveValue = true;
			if (inactiveString != null && inactiveString.equalsIgnoreCase("yes")) {
				isActiveValue = false;
			}
			coach.setIsActive(isActiveValue);
			coach.setCellPhone(remoteCoach.getCellPhone());
			coach.setPhone(remoteCoach.getHomePhone());
			coach.setEmail(remoteCoach.getEmailAddress());
			Location location = new Location();
			location.setCity(remoteCoach.getHomeCity());
			location.setState(remoteCoach.getHomeState());
			location.setZipCode(remoteCoach.getHomeZipCode());
			coach.setLocation(location);
			coach.setGender(CivicoreGenderStringParser.parseGenderString(remoteCoach.getGender()));
			coach.setForeignLanguages(remoteCoach.getForeignLanguage());
			coach.setSkillsExperience(remoteCoach.getSkillsExperience());
			coach.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteCoach.getCreated()).getMillis());
			coach.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteCoach.getUpdated()).getMillis());
		}
		return coach;
	}

	public static List<Coach> fromRemoteCoachList(List<RemoteCoach> remoteCoachList) {
		List<Coach> coaches = null;
		if (remoteCoachList != null) {
			coaches = new ArrayList<Coach>(remoteCoachList.size());
			for (RemoteCoach remoteCoach : remoteCoachList) {
				Coach coach = fromRemoteCoach(remoteCoach);
				coaches.add(coach);
			}

		} else {
			coaches = new ArrayList<Coach>();
		}
		return coaches;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getForeignLanguages() {
		return foreignLanguages;
	}

	public void setForeignLanguages(String foreignLanguages) {
		this.foreignLanguages = foreignLanguages;
	}

	public String getSkillsExperience() {
		return skillsExperience;
	}

	public void setSkillsExperience(String skillsExperience) {
		this.skillsExperience = skillsExperience;
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

}
