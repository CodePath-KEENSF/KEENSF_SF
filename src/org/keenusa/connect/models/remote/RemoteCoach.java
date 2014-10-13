package org.keenusa.connect.models.remote;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class RemoteCoach {

	@Attribute(name = "id")
	private String remoteId;

	@Attribute(name = "created")
	private String created;

	@Attribute(name = "updated")
	private String updated;

	@Element(name = "firstName")
	private String firstName;

	@Element(name = "middleName", required = false)
	private String middleName;

	@Element(name = "lastName")
	private String lastName;

	@Element(name = "dob", required = false)
	private String dateOfbirth;

	@Element(name = "inactive", required = false)
	private String inactive;

	@Element(name = "gender", required = false)
	private String gender;

	@Element(name = "emailAddress", required = false)
	private String emailAddress;

	@Element(name = "cellPhone", required = false)
	private String cellPhone;

	@Element(name = "homePhone", required = false)
	private String homePhone;

	@Element(name = "homeCity", required = false)
	private String homeCity;

	@Element(name = "homeState", required = false)
	private String homeState;

	@Element(name = "homeZipCode", required = false)
	private String homeZipCode;

	public RemoteCoach() {
		super();
	}

	public RemoteCoach(String remoteId, String created, String updated, String firstName, String middleName, String lastName, String dateOfbirth,
			String inactive, String gender, String emailAddress, String cellPhone, String homePhone, String homeCity, String homeState,
			String homeZipCode) {
		super();
		this.remoteId = remoteId;
		this.created = created;
		this.updated = updated;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.dateOfbirth = dateOfbirth;
		this.inactive = inactive;
		this.gender = gender;
		this.emailAddress = emailAddress;
		this.cellPhone = cellPhone;
		this.homePhone = homePhone;
		this.homeCity = homeCity;
		this.homeState = homeState;
		this.homeZipCode = homeZipCode;
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

	public String getCreated() {
		return created;
	}

	public String getUpdated() {
		return updated;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getDateOfbirth() {
		return dateOfbirth;
	}

	public String getInactive() {
		return inactive;
	}

	public String getGender() {
		return gender;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public String getHomeState() {
		return homeState;
	}

	public String getHomeZipCode() {
		return homeZipCode;
	}

}
