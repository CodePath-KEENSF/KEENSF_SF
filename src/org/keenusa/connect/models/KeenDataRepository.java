package org.keenusa.connect.models;

import java.util.List;

public class KeenDataRepository {

	private static KeenDataRepository instance = new KeenDataRepository();

	private List<KeenProgram> keenProgramList;
	private List<KeenSession> keenSessionList;
	private List<Athlete> athleteList;
	private List<Coach> coachList;
	private List<AthleteAttendance> athleteAttendanceList;
	private List<CoachAttendance> coachAttendanceList;
	private List<Athlete> enrolledAthletes;
	private List<KeenProgramEnrolment> programEnrolmentList; 
	private List<Affiliate> affiliateList;

	private KeenDataRepository() {
	}

	public static KeenDataRepository getInstance() {
		return instance;
	}

	public List<KeenProgram> getKeenProgramList() {
		return keenProgramList;
	}

	public void setKeenProgramList(List<KeenProgram> keenProgramList) {
		this.keenProgramList = keenProgramList;
	}

	public List<KeenSession> getKeenSessionList() {
		return keenSessionList;
	}

	public void setKeenSessionList(List<KeenSession> keenSessionList) {
		this.keenSessionList = keenSessionList;
	}

	public List<Athlete> getAthleteList() {
		return athleteList;
	}

	public void setAthleteList(List<Athlete> athleteList) {
		this.athleteList = athleteList;
	}

	public List<Coach> getCoachList() {
		return coachList;
	}

	public void setCoachList(List<Coach> coachList) {
		this.coachList = coachList;
	}

	public List<AthleteAttendance> getAthleteAttendanceList() {
		return athleteAttendanceList;
	}

	public void setAthleteAttendanceList(
			List<AthleteAttendance> athleteAttendanceList) {
		this.athleteAttendanceList = athleteAttendanceList;
	}

	public List<CoachAttendance> getCoachAttendanceList() {
		return coachAttendanceList;
	}

	public void setCoachAttendanceList(List<CoachAttendance> coachAttendanceList) {
		this.coachAttendanceList = coachAttendanceList;
	}

	public List<Athlete> getEnrolledAthletes() {
		return enrolledAthletes;
	}

	public void setEnrolledAthletes(List<Athlete> enrolledAthletes) {
		this.enrolledAthletes = enrolledAthletes;
	}

	public List<KeenProgramEnrolment> getProgramEnrolmentList() {
		return programEnrolmentList;
	}

	public void setProgramEnrolmentList(
			List<KeenProgramEnrolment> programEnrolmentList) {
		this.programEnrolmentList = programEnrolmentList;
	}

	public List<Affiliate> getAffiliateList() {
		return affiliateList;
	}

	public void setAffiliateList(List<Affiliate> affiliateList) {
		this.affiliateList = affiliateList;
	}



}
