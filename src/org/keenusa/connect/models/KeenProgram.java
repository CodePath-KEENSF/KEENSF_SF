package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.helpers.CivicoreDateStringParser;
import org.keenusa.connect.helpers.CivicoreGeneralProgramTypeStringParser;
import org.keenusa.connect.models.remote.RemoteProgram;

public class KeenProgram implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3835920568930700515L;
	private long remoteId;
	private String name;
	private DateTime activeFromDate;
	private DateTime activeToDate;
	private GeneralProgramType generalProgramType;
	private String programTimes;
	private String coachRegistrationConfirmationEmailText;
	// probably we do not need this as it is sent on approval of a new coach, not sure why approval is for a program
	private String coachApprovalConfirmationEmailText;
	private Location location;
	private List<Athlete> enrolledAthletes;

	//in remote source lookup 15
	public enum GeneralProgramType {
		SPORTS, RECREATION, SPECIAL_EVENTS
	}

	public KeenProgram() {
	}

	public KeenProgram(long remoteId, String name, DateTime activeFromDate, DateTime activeToDate, GeneralProgramType generalProgramType,
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

	public static KeenProgram fromRemoteProgram(RemoteProgram remoteProgram) {
		KeenProgram keenProgram = null;
		if (remoteProgram != null) {
			keenProgram = new KeenProgram();
			keenProgram.setRemoteId(Long.valueOf(remoteProgram.getRemoteId()));

			Location location = new Location();
			location.setAddress1(remoteProgram.getAddress1());
			location.setAddress2(remoteProgram.getAddress2());
			location.setCity(remoteProgram.getCity());
			location.setZipCode(remoteProgram.getZipCode());
			location.setState(remoteProgram.getState());
			keenProgram.setLocation(location);

			keenProgram.setName(remoteProgram.getClassName());
			keenProgram.setActiveFromDate(CivicoreDateStringParser.parseDate(remoteProgram.getClassStartDate()));
			keenProgram.setActiveToDate(CivicoreDateStringParser.parseDate(remoteProgram.getClassEndDate()));
			keenProgram.setProgramTimes(remoteProgram.getTimes());
			keenProgram.setGeneralProgramType(CivicoreGeneralProgramTypeStringParser.parseGenralProgramTypeString(remoteProgram
					.getGeneralProgramType()));
			keenProgram.setCoachApprovalConfirmationEmailText(remoteProgram.getApprovalEmailMessage());
			keenProgram.setCoachRegistrationConfirmationEmailText(remoteProgram.getRegistrationConfirmation());
		}
		return keenProgram;
	}

	public static List<KeenProgram> fromRemoteProgramList(List<RemoteProgram> remoteProgramList) {
		List<KeenProgram> programs = null;
		if (remoteProgramList != null) {
			programs = new ArrayList<KeenProgram>(remoteProgramList.size());
			for (RemoteProgram remoteProgram : remoteProgramList) {
				KeenProgram keenProgram = fromRemoteProgram(remoteProgram);
				programs.add(keenProgram);
			}

		} else {
			programs = new ArrayList<KeenProgram>();
		}
		return programs;
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

	public String getCoachRegistrationConfirmationEmailText() {
		return coachRegistrationConfirmationEmailText;
	}

	public void setCoachRegistrationConfirmationEmailText(String coachRegistrationConfirmationEmailText) {
		this.coachRegistrationConfirmationEmailText = coachRegistrationConfirmationEmailText;
	}

	public String getCoachApprovalConfirmationEmailText() {
		return coachApprovalConfirmationEmailText;
	}

	public void setCoachApprovalConfirmationEmailText(String coachApprovalConfirmationEmailText) {
		this.coachApprovalConfirmationEmailText = coachApprovalConfirmationEmailText;
	}

}
