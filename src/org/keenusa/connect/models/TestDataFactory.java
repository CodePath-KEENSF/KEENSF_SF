package org.keenusa.connect.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import android.util.Log;

public class TestDataFactory {

	public static final String DATE_FORMAT = "MM/dd/yyyy";

	private static TestDataFactory instance = new TestDataFactory();
	private Affliliate affliliate;
	private Location contactLocation;
	private Location programLocation;

	private TestDataFactory() {
		affliliate = new Affliliate(1, "KEEN SF", "Damien Chacona", "damien@keensf.com", "www.keensanfrancisco.org");
		contactLocation = new Location("San Francisco", "94102", "CA", "", "");
		programLocation = new Location("San Francisco", "94105", "CA", "Embarcadero YMCA", "169 Steuart Street");
	}

	public static TestDataFactory getInstance() {
		return instance;
	}

	public KeenProgram getProgram() {
		return new KeenProgram(1, "BASKETBALL CLINIC", parseDate("01/01/2012"), parseDate("01/01/2015"), KeenProgram.GeneralProgramType.SPORTS, "1pm-2pm",
				programLocation, getAthleteList());
	}

	public List<Athlete> getAthleteList() {
		ArrayList<Athlete> athletes = new ArrayList<Athlete>();

		Parent primaryParent = new Parent(Parent.ParentRelationship.MOTHER, true, "Lisa", "", "Francisco", "l.fra@mail.com", "6500000001",
				"6500000001", null);
		Athlete athlete = new Athlete(1, "Niki", ContactPerson.Gender.FEMALE, parseDate("01/01/2001"), "English", true, contactLocation,
				primaryParent, "Nikita", "", "Francisco", "n.fra@mail.com", "6500000001", "");
		athletes.add(athlete);

		primaryParent = new Parent(Parent.ParentRelationship.FATHER, true, "John", "Jay", "Ericson", "jee12@mail.com", "6500000002", "6500000002",
				null);
		athlete = new Athlete(2, "Jay", ContactPerson.Gender.MALE, parseDate("10/30/2009"), "Spanish", true, contactLocation, primaryParent, "Jay",
				"John", "Ericson", "jee12@mail.com", "6500000002", "6500000002");
		athletes.add(athlete);

		primaryParent = new Parent(Parent.ParentRelationship.GRANDPARENT, true, "Elizabeth", "Rose", "Green", "", "6500000003", "", null);
		athlete = new Athlete(3, "Lizi", ContactPerson.Gender.FEMALE, parseDate("06/15/2009"), "German", true, contactLocation, primaryParent,
				"Elizabeth", "", "Green", "", "6500000003", "");
		athletes.add(athlete);

		primaryParent = new Parent(Parent.ParentRelationship.GRANDPARENT, true, "Elizabeth", "Rose", "Green", "", "6500000004", "", null);
		athlete = new Athlete(4, "Lizi", null, parseDate("08/05/2011"), "German", true, contactLocation, primaryParent, "Elizabeth", "", "Green", "",
				"6500000004", "");
		athletes.add(athlete);

		primaryParent = new Parent(Parent.ParentRelationship.FOSTER_PARENT, true, "Eric", "M", "Grey", "4150000000", "6500000005",
				"eric.grey@yahoo.com", null);
		athlete = new Athlete(5, "", ContactPerson.Gender.MALE, parseDate("02/02/2000"), "English", true, contactLocation, primaryParent, "Dusty",
				"", "Lee", "", "6500000005", "4150000000");
		athletes.add(athlete);

		return athletes;
	}

	public List<Coach> getCoachList() {
		ArrayList<Coach> coaches = new ArrayList<Coach>();
		Coach coach = new Coach(1, ContactPerson.Gender.MALE, parseDate("02/02/1970"), true, contactLocation, "Aris", "", "Flopp",
				"af1452@mailo.com", "6500000006", "6500000006");
		coaches.add(coach);
		coach = new Coach(2, ContactPerson.Gender.MALE, parseDate("01/01/1950"), true, contactLocation, "Anthony", "Greg", "Lui", "a.lui@mail.com",
				"", "6500000007");
		coaches.add(coach);
		coach = new Coach(3, ContactPerson.Gender.FEMALE, parseDate("01/01/1990"), true, contactLocation, "Alison", "Luisa", "Ricotta",
				"alica@stop.com", "7650000101", "6500000008");
		coaches.add(coach);
		coach = new Coach(4, ContactPerson.Gender.MALE, parseDate("10/01/1971"), true, contactLocation, "Nick", "", "Biork",
				"nick.biork@law.berkley.com", "6500001111", "");
		coaches.add(coach);
		coach = new Coach(5, ContactPerson.Gender.FEMALE, parseDate("04/11/1979"), true, contactLocation, "Melody", "", "Biork",
				"melody.biork@law.berkley.com", "6500001111", "650010101111");
		coaches.add(coach);
		coach = new Coach(6, ContactPerson.Gender.FEMALE, parseDate("04/11/1979"), true, contactLocation, "Rosa", "Maria", "Sanchez",
				"sanchez45@rio.com", "6500002222", "6500002222");
		coaches.add(coach);

		return coaches;
	}

	public List<KeenSession> getSessionList() {
		ArrayList<KeenSession> sessions = new ArrayList<KeenSession>();
		KeenSession session;
		// program contains list of enrolled athletes (registered athletes)
		// for a coach once he/she is registered for a session an attendance record is created with AttendanceValue.REGISTERED
		session = new KeenSession(1, parseDate("10/26/2014"), getProgram(), true, 0, 5, getAthleteAttendanceList(), getCoachAttendanceList());
		sessions.add(session);
		session = new KeenSession(1, parseDate("11/02/2014"), getProgram(), true, 0, 5, getAthleteAttendanceList(), getCoachAttendanceList());
		sessions.add(session);
		session = new KeenSession(1, parseDate("11/09/2014"), getProgram(), true, 0, 5, getAthleteAttendanceList(), getCoachAttendanceList());
		sessions.add(session);
		session = new KeenSession(1, parseDate("11/16/2014"), getProgram(), true, 0, 5, getAthleteAttendanceList(), getCoachAttendanceList());
		sessions.add(session);
		
		return sessions;
	}

	private List<CoachAttendance> getCoachAttendanceList() {
		ArrayList<CoachAttendance> coachAttendanceList = new ArrayList<CoachAttendance>();
		List<Coach> coaches = getCoachList();
		CoachAttendance coachAttendanceRecord = new CoachAttendance(1, 1, coaches.get(0), "", CoachAttendance.AttendanceValue.REGISTERED);
		coachAttendanceList.add(coachAttendanceRecord);
		coachAttendanceRecord = new CoachAttendance(1, 1, coaches.get(1), "", CoachAttendance.AttendanceValue.CANCELLED);
		coachAttendanceList.add(coachAttendanceRecord);
		coachAttendanceRecord = new CoachAttendance(1, 1, coaches.get(2), "sick", CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE);
		coachAttendanceList.add(coachAttendanceRecord);
		coachAttendanceRecord = new CoachAttendance(1, 1, coaches.get(3), "", CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
		coachAttendanceList.add(coachAttendanceRecord);
		coachAttendanceRecord = new CoachAttendance(1, 1, coaches.get(4), "", CoachAttendance.AttendanceValue.ATTENDED);
		coachAttendanceList.add(coachAttendanceRecord);
		return coachAttendanceList;
	}

	private List<AthleteAttendance> getAthleteAttendanceList() {
		ArrayList<AthleteAttendance> athleteAttendanceList = new ArrayList<AthleteAttendance>();
		List<Athlete> athletes = getAthleteList();
		AthleteAttendance athleteAttendanceRecord = new AthleteAttendance(1, 1, athletes.get(0), AthleteAttendance.AttendanceValue.ATTENDED);
		athleteAttendanceList.add(athleteAttendanceRecord);
		athleteAttendanceRecord = new AthleteAttendance(1, 1, athletes.get(1), AthleteAttendance.AttendanceValue.ATTENDED);
		athleteAttendanceList.add(athleteAttendanceRecord);
		//		athlete does not have attendance record but is registered for a program so he/she has not been checked-in yet
		//		athleteAttendanceRecord = new AthleteAttendance(1, 1, athletes.get(2), null);
		//		athleteAttendanceList.add(athleteAttendanceRecord);
		athleteAttendanceRecord = new AthleteAttendance(1, 1, athletes.get(3), AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW);
		athleteAttendanceList.add(athleteAttendanceRecord);
		athleteAttendanceRecord = new AthleteAttendance(1, 1, athletes.get(4), AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE);
		athleteAttendanceList.add(athleteAttendanceRecord);
		return athleteAttendanceList;
	}

	public Affliliate getAffliliate() {
		return affliliate;

	}

	public Location getLocation() {
		return contactLocation;

	}

	private DateTime parseDate(String stringDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
		dateFormat.setLenient(true);
		DateTime date = null;
		try {
			date = new DateTime(dateFormat.parse(stringDate).getTime());
		} catch (ParseException pe) {
			Log.e("DATE_PARSING", pe.toString());
		}
		return date;
	}
}
