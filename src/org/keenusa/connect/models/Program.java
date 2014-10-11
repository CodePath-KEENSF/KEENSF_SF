package org.keenusa.connect.models;

import java.util.List;

import org.joda.time.DateTime;

public class Program {

	private long remoteId;
	private String name;
	private DateTime activeFromDate;
	private DateTime activeToDate;
	private GeneralProgramType generalProgramType;
	private String programTimes;
	private Location location;
	private List<Athlete> enrolledAthletes;

	//in remote source lookup 15
	public enum GeneralProgramType {
		SPORTS, RECREATION, SPECIAL_EVENTS
	}

	public Program() {
	}

	public Program(long remoteId, String name, DateTime activeFromDate, DateTime activeToDate, GeneralProgramType generalProgramType,
			String programTimes, Location location, List<Athlete> enrolledAthletes) {
		super();
		this.remoteId = remoteId;
		this.name = name;
		this.activeFromDate = activeFromDate;
		this.activeToDate = activeToDate;
		this.generalProgramType = generalProgramType;
		this.programTimes = programTimes;
		this.location = location;
		this.enrolledAthletes = enrolledAthletes;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getActiveFromDate() {
		return activeFromDate;
	}

	public void setActiveFromDate(DateTime activeFromDate) {
		this.activeFromDate = activeFromDate;
	}

	public DateTime getActiveToDate() {
		return activeToDate;
	}

	public void setActiveToDate(DateTime activeToDate) {
		this.activeToDate = activeToDate;
	}

	public GeneralProgramType getGeneralProgramType() {
		return generalProgramType;
	}

	public void setGeneralProgramType(GeneralProgramType generalProgramType) {
		this.generalProgramType = generalProgramType;
	}

	public String getProgramTimes() {
		return programTimes;
	}

	public void setProgramTimes(String programTimes) {
		this.programTimes = programTimes;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Athlete> getEnrolledAthletes() {
		return enrolledAthletes;
	}

	public void setEnrolledAthletes(List<Athlete> enrolledAthletes) {
		this.enrolledAthletes = enrolledAthletes;
	}

}
