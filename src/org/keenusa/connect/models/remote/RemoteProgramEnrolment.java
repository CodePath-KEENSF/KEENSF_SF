package org.keenusa.connect.models.remote;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteProgramEnrolment {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	// program with name and id
	@ElementMap(entry = "classes_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> programMap;

	@Element(name = "waitlist", required = false)
	private String waitlist;

	// athlete with name and id
	@ElementMap(entry = "youth_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> youthMap;

	public RemoteProgramEnrolment() {
		super();
	}

	public RemoteProgramEnrolment(String remoteId, String created, String updated, Map<String, String> programMap, String waitlist,
			Map<String, String> youthMap) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.programMap = programMap;
		this.waitlist = waitlist;
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

	public Map<String, String> getProgramMap() {
		return programMap;
	}

	public String getWaitlist() {
		return waitlist;
	}

	public Map<String, String> getYouthMap() {
		return youthMap;
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
