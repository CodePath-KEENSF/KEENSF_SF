package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class KeenSession implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5601978164288806597L;
	private long remoteId;
	private DateTime date;
	private KeenProgram program;

	private boolean openToPublicRegistration;
	private int numberOfNewCoachesNeeded;
	private int numberOfReturningCoachesNeeded;

	private List<AthleteAttendance> athleteAttendance;
	private List<CoachAttendance> coachAttendance;

	public KeenSession() {
		super();
	}

	public KeenSession(long remoteId, DateTime date, KeenProgram program, boolean openToPublicRegistration, int numberOfNewCoachesNeeded,
			int numberOfReturningCoachesNeeded, List<AthleteAttendance> athleteAttendance, List<CoachAttendance> coachAttendance) {
		super();
		this.remoteId = remoteId;
		this.date = date;
		this.program = program;
		this.openToPublicRegistration = openToPublicRegistration;
		this.numberOfNewCoachesNeeded = numberOfNewCoachesNeeded;
		this.numberOfReturningCoachesNeeded = numberOfReturningCoachesNeeded;
		this.athleteAttendance = athleteAttendance;
		this.coachAttendance = coachAttendance;
	}

	public int getRegisteredAthleteCount() {
		int registeredAthleteCount = 0;
		if (program != null && program.getEnrolledAthletes() != null) {
			registeredAthleteCount = program.getEnrolledAthletes().size();
		}
		return registeredAthleteCount;
	}

	public int getRegisteredCoachCount() {
		int registeredCoachCount = 0;
		if (coachAttendance != null) {
			for (CoachAttendance attendance : coachAttendance) {
				if (attendance.getAttendanceValue() == CoachAttendance.AttendanceValue.REGISTERED) {
					registeredCoachCount++;
				}
			}
		}

		return registeredCoachCount;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public boolean isOpenToPublicRegistration() {
		return openToPublicRegistration;
	}

	public void setOpenToPublicRegistration(boolean openToPublicRegistration) {
		this.openToPublicRegistration = openToPublicRegistration;
	}

	public KeenProgram getProgram() {
		return program;
	}

	public void setProgram(KeenProgram program) {
		this.program = program;
	}

	public List<AthleteAttendance> getAthleteAttendance() {
		return athleteAttendance;
	}

	public void setAthleteAttendance(List<AthleteAttendance> athleteAttendance) {
		this.athleteAttendance = athleteAttendance;
	}

	public List<CoachAttendance> getCoachAttendance() {
		return coachAttendance;
	}

	public void setCoachAttendance(List<CoachAttendance> coachAttendance) {
		this.coachAttendance = coachAttendance;
	}

	public int getNumberOfReturningCoachesNeeded() {
		return numberOfReturningCoachesNeeded;
	}

	public void setNumberOfReturningCoachesNeeded(int numberOfReturningCoachesNeeded) {
		this.numberOfReturningCoachesNeeded = numberOfReturningCoachesNeeded;
	}

	public int getNumberOfNewCoachesNeeded() {
		return numberOfNewCoachesNeeded;
	}

	public void setNumberOfNewCoachesNeeded(int numberOfNewCoachesNeeded) {
		this.numberOfNewCoachesNeeded = numberOfNewCoachesNeeded;
	}

}
