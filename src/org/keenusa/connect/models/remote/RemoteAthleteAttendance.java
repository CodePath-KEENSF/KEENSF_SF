package org.keenusa.connect.models.remote;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteAthleteAttendance {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	// program with name and id
	@ElementMap(entry = "classes_days_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> classesDaysMap;

	@Element(name = "attendance", required = false)
	private String attendance;

	// athlete with name and id
	@ElementMap(entry = "youth_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> youthMap;

	public RemoteAthleteAttendance() {
		super();
	}

	public RemoteAthleteAttendance(String remoteId, String created, String updated, Map<String, String> classesDaysMap, String attendance,
			Map<String, String> youthMap) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.classesDaysMap = classesDaysMap;
		this.attendance = attendance;
		this.youthMap = youthMap;
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

	public Map<String, String> getClassesDaysMap() {
		return classesDaysMap;
	}

	public String getAttendance() {
		return attendance;
	}

	public Map<String, String> getYouthMap() {
		return youthMap;
	}

	public String getClassesDaysId() {
		if (getClassesDaysMap() != null && getClassesDaysMap().keySet() != null) {
			for (String key : getClassesDaysMap().keySet()) {
				return key;
			}
		}
		return null;
	}

	public String getYouthId() {
		if (getYouthMap() != null && getYouthMap().keySet() != null) {
			for (String key : getYouthMap().keySet()) {
				return key;
			}
		}
		return null;
	}

	public String getYouthName() {
		if (getYouthMap() != null && getYouthMap().values() != null) {
			for (String value : getYouthMap().values()) {
				return value;
			}
		}
		return null;
	}

}
