package org.keenusa.connect.models.remote;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteCoachAttendance {

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

	@Element(name = "comments", required = false)
	private String comments;

	// coach with name and id
	@ElementMap(entry = "contacts_id", key = "record_id", attribute = true, inline = true)
	private Map<String, String> contactsMap;

	public RemoteCoachAttendance() {
		super();
	}

	public RemoteCoachAttendance(String remoteId, String created, String updated, Map<String, String> classesDaysMap, String attendance,
			String comments, Map<String, String> contactsMap) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.classesDaysMap = classesDaysMap;
		this.attendance = attendance;
		this.comments = comments;
		this.contactsMap = contactsMap;
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

	public String getComments() {
		return comments;
	}

	public Map<String, String> getContactsMap() {
		return contactsMap;
	}

	public String getClassesDaysId() {
		if (getClassesDaysMap() != null && getClassesDaysMap().keySet() != null) {
			for (String key : getClassesDaysMap().keySet()) {
				return key;
			}
		}
		return null;
	}

	public String getContactId() {
		if (getContactsMap() != null && getContactsMap().keySet() != null) {
			for (String key : getContactsMap().keySet()) {
				return key;
			}
		}
		return null;
	}

	public String getContactName() {
		if (getContactsMap() != null && getContactsMap().values() != null) {
			for (String value : getContactsMap().values()) {
				return value;
			}
		}
		return null;
	}

}
