package org.keenusa.connect.networking;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteAthlete {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "firstName")
	private String firstName;

	@Element(name = "lastName")
	private String lastName;

	public RemoteAthlete() {
		super();
	}

	public RemoteAthlete(String remoteId, String firstName, String lastName) {
		this.remoteId = remoteId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}