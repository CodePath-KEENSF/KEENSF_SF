package org.keenusa.connect.models.remote;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteSession {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "attendanceDate")
	private String attendanceDate;

	// program with name and id
	@ElementMap(entry = "classes_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> programMap;

	// number of athletes "registered" i.e. enrolled to a program
	@Element(name = "athletes", required = false)
	private String athletes;

	// number of coaches attended
	@Element(name = "coachesAttended", required = false)
	private String coachesAttended;

	// number of coaches registered
	@Element(name = "coachesRegistered", required = false)
	private String coachesRegistered;

	// number of new coaches needed should be combined with returning coaches
	@Element(name = "newCoachesNeeded", required = false)
	private String newCoachesNeeded;

	@Element(name = "openToPublicRegistration", required = false)
	private String openToPublicRegistration;

	@Element(name = "returningCoachesNeeded", required = false)
	private String returningCoachesNeeded;

	public RemoteSession() {
		super();
	}

	public RemoteSession(String remoteId, String created, String updated, String attendanceDate, Map<String, String> programMap, String athletes,
			String coachesAttended, String coachesRegistered, String newCoachesNeeded, String openToPublicRegistration, String returningCoachesNeeded) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.attendanceDate = attendanceDate;
		this.programMap = programMap;
		this.athletes = athletes;
		this.coachesAttended = coachesAttended;
		this.coachesRegistered = coachesRegistered;
		this.newCoachesNeeded = newCoachesNeeded;
		this.openToPublicRegistration = openToPublicRegistration;
		this.returningCoachesNeeded = returningCoachesNeeded;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

	public String getAttendanceDate() {
		return attendanceDate;
	}

	public Map<String, String> getProgramMap() {
		return programMap;
	}

	public String getAthletes() {
		return athletes;
	}

	public String getCoachesAttended() {
		return coachesAttended;
	}

	public String getCoachesRegistered() {
		return coachesRegistered;
	}

	public String getNewCoachesNeeded() {
		return newCoachesNeeded;
	}

	public String getOpenToPublicRegistration() {
		return openToPublicRegistration;
	}

	public String getReturningCoachesNeeded() {
		return returningCoachesNeeded;
	}

	public String getClassesId() {
		if (getProgramMap() != null && getProgramMap().keySet() != null) {
			for (String key : getProgramMap().keySet()) {
				return key;
			}
		}
		return null;
	}

	public String getClassesName() {
		if (getProgramMap() != null && getProgramMap().values() != null) {
			for (String value : getProgramMap().values()) {
				return value;
			}
		}
		return null;
	}

}
