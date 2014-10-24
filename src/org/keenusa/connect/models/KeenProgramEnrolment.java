package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.helpers.CivicoreWaitlistStringParser;
import org.keenusa.connect.models.remote.RemoteProgramEnrolment;

public class KeenProgramEnrolment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486907615451742803L;
	private long id;
	private long remoteId;
	private KeenProgram program;
	private Athlete athlete;
	private boolean isInWaitlist;
	private String athleteFullName;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;

	public KeenProgramEnrolment() {
	}

	public KeenProgramEnrolment(long remoteId, KeenProgram program, Athlete athlete, boolean isInWaitlist) {
		super();
		this.remoteId = remoteId;
		this.program = program;
		this.athlete = athlete;
		this.isInWaitlist = isInWaitlist;
	}

	public static KeenProgramEnrolment fromRemoteProgramEnrolment(RemoteProgramEnrolment remoteProgramEnrolment) {
		KeenProgramEnrolment programEnrolment = null;
		if (remoteProgramEnrolment != null) {
			programEnrolment = new KeenProgramEnrolment();
			programEnrolment.setRemoteId(Long.valueOf(remoteProgramEnrolment.getRemoteId()));
			programEnrolment.setInWaitlist(CivicoreWaitlistStringParser.parseWaitlistString(remoteProgramEnrolment.getWaitlist()));

			Athlete athlete = new Athlete();
			athlete.setRemoteId(Long.valueOf(remoteProgramEnrolment.getYouthId()));
			programEnrolment.setAthlete(athlete);
			programEnrolment.setAthleteFullName(remoteProgramEnrolment.getYouthName());

			KeenProgram program = new KeenProgram();
			program.setRemoteId(Long.valueOf(remoteProgramEnrolment.getClassesId()));
			program.setName(remoteProgramEnrolment.getClassesName());
			programEnrolment.setProgram(program);
			programEnrolment.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteProgramEnrolment.getCreated()).getMillis());
			programEnrolment.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteProgramEnrolment.getUpdated()).getMillis());

		}
		return programEnrolment;
	}

	public static List<KeenProgramEnrolment> fromRemoteProgramEnrolmentList(List<RemoteProgramEnrolment> remoteProgramEnrolmentList) {
		List<KeenProgramEnrolment> programEnrolments = null;
		if (remoteProgramEnrolmentList != null) {
			programEnrolments = new ArrayList<KeenProgramEnrolment>(remoteProgramEnrolmentList.size());
			for (RemoteProgramEnrolment remoteProgramEnrolment : remoteProgramEnrolmentList) {
				KeenProgramEnrolment programEnrolment = fromRemoteProgramEnrolment(remoteProgramEnrolment);
				programEnrolments.add(programEnrolment);
			}

		} else {
			programEnrolments = new ArrayList<KeenProgramEnrolment>();
		}
		return programEnrolments;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public KeenProgram getProgram() {
		return program;
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public boolean isInWaitlist() {
		return isInWaitlist;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public void setProgram(KeenProgram program) {
		this.program = program;
	}

	public void setAthlete(Athlete athlete) {
		this.athlete = athlete;
	}

	public void setInWaitlist(boolean isInWaitlist) {
		this.isInWaitlist = isInWaitlist;
	}

	private String getAthleteFullName() {
		return athleteFullName;
	}

	private void setAthleteFullName(String athleteFullName) {
		this.athleteFullName = athleteFullName;
	}

	public String getEnrolledAthleteFullName() {
		if (getAthlete() != null && getAthlete().getFullName() != null && !getAthlete().getFullName().isEmpty()) {
			return getAthlete().getFullName();
		} else {
			return getAthleteFullName();
		}
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

}
